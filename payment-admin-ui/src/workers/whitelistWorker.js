/* eslint-disable no-restricted-globals */
import * as XLSX from 'xlsx'

const DEFAULT_CHUNK_SIZE = 2000
const FULL_CARD_MIN_LENGTH = 16
const FULL_CARD_MAX_LENGTH = 19

const FIELD_LABELS = {
  fullNumber: '【完整卡号】',
  bin: '【卡前六位】',
  lastFour: '【卡后四位】',
  riskLevel: '【风险等级】',
  merchantIds: '【关联商户编号】'
}

self.onmessage = event => {
  const { type, payload } = event.data || {}
  if (type === 'PROCESS_FILE') {
    processFile(payload || {})
  }
}

function processFile(payload) {
  const { buffer, headersMap = {}, locale = 'zh', chunkSize = DEFAULT_CHUNK_SIZE, taskId } = payload
  if (!buffer) {
    return postError(taskId, '未收到待解析的文件数据')
  }
  try {
    const data = new Uint8Array(buffer)
    const workbook = XLSX.read(data, { type: 'array' })
    const firstSheetName = workbook.SheetNames[0]
    if (!firstSheetName) {
      return postError(taskId, 'Excel 中未找到任何工作表')
    }
    const worksheet = workbook.Sheets[firstSheetName]
    const range = XLSX.utils.decode_range(worksheet['!ref'])
    const headerRange = {
      s: { c: range.s.c, r: range.s.r },
      e: { c: range.e.c, r: range.s.r }
    }
    const [headerRow] = XLSX.utils.sheet_to_json(worksheet, { header: 1, range: headerRange, raw: false }) || []
    const normalizedHeader = normalizeHeaderRow(headerRow)
    const expectedHeader = headersMap[locale]
    if (!expectedHeader) {
      return postError(taskId, '无法确定当前语言的模板格式')
    }
    if (!headersEqual(normalizedHeader, expectedHeader)) {
      const matchedLocale = Object.keys(headersMap).find(key => headersEqual(normalizedHeader, headersMap[key]))
      if (matchedLocale) {
        return postError(taskId, '模板语言与当前设置不一致，请下载匹配语言的模板后再试')
      }
      return postError(taskId, '模板列名不匹配，请下载最新模板后上传')
    }

    const totalRowsRaw = Math.max(range.e.r - range.s.r, 0)
    const duplicateMap = new Map()
    const effectiveChunkSize = Math.max(chunkSize || DEFAULT_CHUNK_SIZE, 1)
    let processedRows = 0
    let validRowCount = 0

    for (let startRow = range.s.r + 1; startRow <= range.e.r; startRow += effectiveChunkSize) {
      const endRow = Math.min(range.e.r, startRow + effectiveChunkSize - 1)
      const rows = XLSX.utils.sheet_to_json(worksheet, {
        header: 1,
        raw: false,
        range: { s: { c: range.s.c, r: startRow }, e: { c: range.e.c, r: endRow } }
      })
      for (let i = 0; i < rows.length; i++) {
        const excelRowNumber = startRow + i + 1
        const row = rows[i]
        if (isRowEmpty(row)) {
          continue
        }
        validRowCount += 1
        const record = normalizeRecord(row)
        const validationResult = validateRecord(record)
        if (validationResult) {
          const normalizedError = normalizeValidationResult(validationResult)
          const metaWithRow = Object.assign({ rowNumber: excelRowNumber }, normalizedError.meta || {})
          const messageWithRow = normalizedError.message
            ? `第${excelRowNumber}行${normalizedError.message}`
            : `第${excelRowNumber}行数据格式不正确`
          return postError(taskId, messageWithRow, normalizedError.code, metaWithRow)
        }
        const duplicateKey = buildDuplicateKey(record)
        if (!duplicateMap.has(duplicateKey)) {
          duplicateMap.set(duplicateKey, [])
        }
        duplicateMap.get(duplicateKey).push(excelRowNumber)
      }
      processedRows += rows.length
      self.postMessage({
        type: 'progress',
        payload: {
          processedRows: Math.min(processedRows, totalRowsRaw),
          totalRows: totalRowsRaw,
          taskId
        }
      })
    }

    const duplicateLines = collectDuplicateLines(duplicateMap)
    if (duplicateLines.length > 0) {
      const preview = duplicateLines.slice(0, 10).join('、')
      return postError(taskId, `检测到重复数据，请检查以下行号：${preview}`)
    }

    self.postMessage({
      type: 'success',
      payload: {
        totalRows: validRowCount,
        rawTotalRows: totalRowsRaw,
        taskId
      }
    })
  } catch (error) {
    postError(taskId, error && error.message ? error.message : '解析失败，请重试')
  }
}

function normalizeHeaderRow(row = []) {
  return row.map(cell => (cell == null ? '' : String(cell).trim()))
}

function headersEqual(source = [], target = []) {
  if (!Array.isArray(source) || !Array.isArray(target) || source.length !== target.length) {
    return false
  }
  for (let i = 0; i < source.length; i++) {
    if (source[i] !== target[i]) {
      return false
    }
  }
  return true
}

function isRowEmpty(row) {
  if (!Array.isArray(row)) {
    return true
  }
  return row.every(cell => cell === undefined || cell === null || String(cell).trim() === '')
}

function normalizeRecord(row = []) {
  return {
    fullNumber: safeString(row[0]),
    bin: safeString(row[1]),
    lastFour: safeString(row[2]),
    riskLevel: safeString(row[3]),
    merchantIds: safeString(row[4])
  }
}

function safeString(value) {
  if (value === undefined || value === null) {
    return ''
  }
  return String(value).trim()
}

function validateRecord(record) {
  const hasFullNumber = !!record.fullNumber
  const hasBin = !!record.bin
  const hasLastFour = !!record.lastFour

  if (hasFullNumber) {
    const sanitizedFullNumber = record.fullNumber.replace(/\s+/g, '')
    const length = sanitizedFullNumber.length
    if (length < FULL_CARD_MIN_LENGTH || length > FULL_CARD_MAX_LENGTH) {
      return {
        code: 'FULL_NUMBER_LENGTH_OUT_OF_RANGE',
        message: `${FIELD_LABELS.fullNumber}长度必须为${FULL_CARD_MIN_LENGTH}-${FULL_CARD_MAX_LENGTH}位，当前为${length}位`,
        meta: {
          expectedMin: FULL_CARD_MIN_LENGTH,
          expectedMax: FULL_CARD_MAX_LENGTH,
          actualLength: length,
          rawValue: record.fullNumber || ''
        }
      }
    }
    const parsed = parseFullNumber(record.fullNumber)
    if (!parsed) {
      return `${FIELD_LABELS.fullNumber}格式错误`
    }
    record.bin = parsed.bin
    record.lastFour = parsed.lastFour
  } else {
    if (!hasBin && !hasLastFour) {
      return `${FIELD_LABELS.fullNumber}或${FIELD_LABELS.bin}${FIELD_LABELS.lastFour}需填写一项`
    }
    if (!hasBin) {
      return `${FIELD_LABELS.bin}不能为空`
    }
    if (!hasLastFour) {
      return `${FIELD_LABELS.lastFour}不能为空`
    }
    if (!/^\d{6}$/.test(record.bin)) {
      return `${FIELD_LABELS.bin}必须是6位数字`
    }
    if (!/^\d{4}$/.test(record.lastFour)) {
      return `${FIELD_LABELS.lastFour}必须是4位数字`
    }
  }

  if (record.riskLevel === '') {
    return `${FIELD_LABELS.riskLevel}不能为空`
  }
  if (Number.isNaN(Number(record.riskLevel))) {
    return `${FIELD_LABELS.riskLevel}必须是数字`
  }

  if (!record.merchantIds) {
    return `${FIELD_LABELS.merchantIds}不能为空`
  }
  if (!/^(\d+)(;\d+)*$/.test(record.merchantIds)) {
    return `${FIELD_LABELS.merchantIds}格式错误`
  }

  return ''
}

function parseFullNumber(value) {
  const sanitized = value.replace(/\s+/g, '')
  if (!sanitized) {
    return null
  }
  if (/^\d{16,19}$/.test(sanitized)) {
    return {
      bin: sanitized.substring(0, 6),
      lastFour: sanitized.substring(sanitized.length - 4),
      raw: sanitized
    }
  }
  const maskedMatch = sanitized.match(/^(\d{6})([\d*]{6,9})(\d{4})$/)
  if (maskedMatch) {
    return {
      bin: maskedMatch[1],
      lastFour: maskedMatch[3],
      raw: sanitized
    }
  }
  return null
}

function buildDuplicateKey(record) {
  if (record.fullNumber && !record.fullNumber.includes('*')) {
    return `FULL-${record.fullNumber.replace(/\s+/g, '')}`
  }
  return `PART-${record.bin}-${record.lastFour}`
}

function collectDuplicateLines(duplicateMap) {
  const duplicates = []
  duplicateMap.forEach(lines => {
    if (lines.length > 1) {
      duplicates.push(...lines)
    }
  })
  duplicates.sort((a, b) => a - b)
  return duplicates
}

function normalizeValidationResult(result) {
  if (!result) {
    return { message: '', code: 'GENERIC', meta: {} }
  }
  if (typeof result === 'string') {
    return { message: result, code: 'GENERIC', meta: {} }
  }
  return {
    message: result.message || '',
    code: result.code || 'GENERIC',
    meta: result.meta || {}
  }
}

function postError(taskId, message, code = 'GENERIC', meta = {}) {
  self.postMessage({
    type: 'error',
    payload: { taskId, message, code, meta }
  })
}
