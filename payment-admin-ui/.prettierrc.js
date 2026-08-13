module.exports = {
  // 打印宽度，超过换行
  printWidth: 120,
  // 缩进大小
  tabWidth: 2,
  // 使用空格而不是制表符缩进
  useTabs: false,
  // 在语句末尾添加分号 (与ESLint保持一致)
  semi: false,
  // 使用单引号而不是双引号
  singleQuote: true,
  // 对象属性引号策略: "as-needed" | "consistent" | "preserve"
  quoteProps: "as-needed",
  // 在 JSX 中使用单引号
  jsxSingleQuote: true,
  // 尾随逗号: "none" | "es5" | "all" (与ESLint保持一致)
  trailingComma: "none",
  // 在对象文字中的括号之间添加空格 (与ESLint保持一致)
  bracketSpacing: true,
  // 将多行 JSX 元素的 > 放在最后一行的末尾，而不是单独放在下一行
  bracketSameLine: false,
  // 箭头函数参数括号: "avoid" | "always"
  arrowParens: "avoid",
  // 换行符: "auto" | "lf" | "crlf" | "cr"
  endOfLine: "lf",
  // Vue 文件中脚本和样式标签缩进
  vueIndentScriptAndStyle: false,
  // HTML 空白敏感性: "css" | "strict" | "ignore"
  htmlWhitespaceSensitivity: "ignore",
  // 强制每行最大属性数 (配合ESLint的vue/max-attributes-per-line)
  singleAttributePerLine: false
}
