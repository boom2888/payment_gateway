/**
 * 获取浏览器信息
 * @returns {Object} 包含浏览器信息的对象
 */
export const getBrowserInfo = () => {
  // 获取时区偏移量
  const getTimeZoneOffset = () => {
    try {
      const date = new Date();
      const offset = date.getTimezoneOffset();
      const hours = Math.abs(Math.floor(offset / 60));
      const minutes = Math.abs(offset % 60);

      // 格式化为 "+08" 或 "-05" 格式
      const sign = offset <= 0 ? '+' : '-';
      const formattedHours = hours.toString().padStart(2, '0');

      // 如果分钟不为0，则包含分钟，否则只返回小时
      if (minutes > 0) {
        const formattedMinutes = minutes.toString().padStart(2, '0');
        return `${sign}${formattedHours}:${formattedMinutes}`;
      } else {
        return `${sign}${formattedHours}`;
      }
    } catch (error) {
      console.warn('获取时区偏移量失败:', error);
      return 'UTC';
    }
  };

  return {
    // Accept header - 浏览器接受的MIME类型
    acceptHeader: navigator.userAgent.includes('Chrome') ?
      'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8' :
      'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',

    // Java是否启用
    javaEnabled: navigator.javaEnabled ? navigator.javaEnabled() : false,

    // JavaScript是否启用
    javaScriptEnabled: true,

    // 浏览器语言
    language: navigator.language || navigator.userLanguage || 'en-US',

    // 颜色深度
    colorDepth: screen.colorDepth || 24,

    // 屏幕高度
    screenHeight: screen.height || 0,

    // 屏幕宽度
    screenWidth: screen.width || 0,

    // 时区偏移量 (格式: +08, -05, +08:30 等，长度在0-5之间)
    timeZone: getTimeZoneOffset(),

    // 用户代理字符串
    userAgent: navigator.userAgent || ''
  };
}; 