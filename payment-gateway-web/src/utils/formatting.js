// 省略字符串前面，保留后面的字符
export const formatString = (str, length = 4) => {
  if (!str) return "";
  const val = str.toString();
  if (val.length > 10) {
    return "..." + val.substr(val.length - length);
  } else {
    return val.padEnd(length, " ");
  }
};

// 省略中间字符串，保留前面的字符和后面的字符
export const formatMiddleString = (str, length = 4) => {
  if (!str) return "";
  const val = str.toString();
  if (val.length > 10) {
    return val.substr(0, length) + "..." + val.substr(val.length - length);
  } else {
    return val;
  }
};
// 保留前面的字符，省略中间字符串，保留后面的字符
export function maskMiddle(str) {
  // return str.replace(/(.?@).+?(@.?)/g, (match, p1, p2) => {
  //   return p1 + '*'.repeat(match.length - p1.length - p2.length) + p2;
  // });
  // 匹配：第一个字符 + 中间任意内容 + 最后一个@及其后的内容
  return str.replace(/^(.).*?(@[^@]*)$/, (match, firstChar, lastPart) => {
    const masked =
      firstChar +
      "*".repeat(str.length - firstChar.length - lastPart.length) +
      lastPart;
    return masked;
  });
}

export const formatMoney = (money) => {
  if (!money) return "0.00";

  let str = parseFloat(money.toString()).toString();
  const dotIndex = str.indexOf(".");

  if (dotIndex !== -1) {
    str = str.substring(0, dotIndex + 3);
    str += "0".repeat(dotIndex + 3 - str.length);
  } else {
    str += ".00";
  }
  return str;
};

// 数据格式转化  {2024-12-17:[]}  => [{data:2024-12-17,transactions:[]}]
export function convertToArray(jsonObject) {
  return Object.entries(jsonObject).map(([date, transactions]) => ({
    date,
    transactions,
  }));
}

// 数字千分位分隔符
export function formatNumber(num) {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
