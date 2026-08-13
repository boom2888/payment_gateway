/**
 * 时区工具类
 * 用于获取客户端时区信息
 */

/**
 * 获取客户端时区
 * @returns {string} 时区字符串，如 'Asia/Shanghai', 'America/New_York' 等
 */
export function getClientTimezone() {
  try {
    // 使用 Intl.DateTimeFormat().resolvedOptions().timeZone 获取客户端时区
    const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone
    return timezone || 'Asia/Shanghai' // 默认使用北京时间
  } catch (error) {
    console.warn('无法获取客户端时区，使用默认时区 Asia/Shanghai:', error)
    return 'Asia/Shanghai'
  }
}

/**
 * 获取时区偏移量（分钟）
 * @returns {number} 时区偏移量（分钟）
 */
export function getTimezoneOffset() {
  return new Date().getTimezoneOffset()
}

/**
 * 格式化时区偏移量为字符串
 * @returns {string} 时区偏移量字符串，如 '+08:00', '-05:00' 等
 */
export function getTimezoneOffsetString() {
  const offset = getTimezoneOffset()
  const sign = offset <= 0 ? '+' : '-'
  const hours = Math.abs(Math.floor(offset / 60))
  const minutes = Math.abs(offset % 60)
  return `${sign}${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`
}

/**
 * 检查时区是否有效
 * @param {string} timezone 时区字符串
 * @returns {boolean} 是否有效
 */
export function isValidTimezone(timezone) {
  try {
    Intl.DateTimeFormat(undefined, { timeZone: timezone })
    return true
  } catch (error) {
    return false
  }
}

/**
 * 获取常用时区列表
 * @returns {Array} 常用时区列表
 */
export function getCommonTimezones() {
  return [
    { value: 'Asia/Shanghai', label: '北京时间 (UTC+8)' },
    { value: 'Asia/Tokyo', label: '东京时间 (UTC+9)' },
    { value: 'Asia/Seoul', label: '首尔时间 (UTC+9)' },
    { value: 'Asia/Singapore', label: '新加坡时间 (UTC+8)' },
    { value: 'Asia/Hong_Kong', label: '香港时间 (UTC+8)' },
    { value: 'America/New_York', label: '纽约时间 (UTC-5/-4)' },
    { value: 'America/Los_Angeles', label: '洛杉矶时间 (UTC-8/-7)' },
    { value: 'America/Chicago', label: '芝加哥时间 (UTC-6/-5)' },
    { value: 'Europe/London', label: '伦敦时间 (UTC+0/+1)' },
    { value: 'Europe/Paris', label: '巴黎时间 (UTC+1/+2)' },
    { value: 'Europe/Berlin', label: '柏林时间 (UTC+1/+2)' },
    { value: 'Australia/Sydney', label: '悉尼时间 (UTC+10/+11)' }
  ]
}
