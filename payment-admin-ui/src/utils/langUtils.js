import { getLanguage } from '@/lang'

/**
 * 获取语言代码
 * 将当前语言映射为后端需要的语言代码格式
 * @returns {string} 语言代码 (zh_CN 或 en_US)
 */
export function getLangCode() {
  const currentLang = getLanguage()
  const langMap = {
    zh: 'zh_CN',
    en: 'en_US'
  }
  return langMap[currentLang] || 'zh_CN'
}

/**
 * 检查当前是否为中文环境
 * @returns {boolean} 是否为中文
 */
export function isZhLang() {
  const currentLang = getLanguage()
  return currentLang === 'zh'
}

/**
 * 检查当前是否为英文环境
 * @returns {boolean} 是否为英文
 */
export function isEnLang() {
  const currentLang = getLanguage()
  return currentLang === 'en'
}
