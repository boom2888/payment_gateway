export const getFileName = function (fileUrl) {
  if (!fileUrl) {
    return ""
  }
  const parts = fileUrl.split("/")
  const fullFileName = parts[parts.length - 1]
  // 移除查询参数
  const fileName = fullFileName.split("?")[0]
  // 如果文件名太长，截断显示
  return fileName.length > 20 ? fileName.substring(0, 17) + "..." : fileName
}

// 获取文件扩展名
export const getFileExtension = function (fileUrl) {
  if (!fileUrl) return ""
  const parts = fileUrl.split(".")
  return parts.length > 1 ? parts[parts.length - 1] : ""
}

// 获取文件图标
export const getFileIcon = function (fileUrl) {
  if (!fileUrl) return "el-icon-document"

  const extension = getFileExtension(fileUrl).toLowerCase()
  const iconMap = {
    // 文档类
    doc: "el-icon-document",
    docx: "el-icon-document",
    txt: "el-icon-document",
    rtf: "el-icon-document",
    // PDF
    pdf: "el-icon-document-copy",
    // 表格类
    xls: "el-icon-s-grid",
    xlsx: "el-icon-s-grid",
    csv: "el-icon-s-grid",
    // 图片类
    jpg: "el-icon-picture",
    jpeg: "el-icon-picture",
    png: "el-icon-picture",
    gif: "el-icon-picture",
    bmp: "el-icon-picture",
    svg: "el-icon-picture",
    // 压缩文件
    zip: "el-icon-folder-opened",
    rar: "el-icon-folder-opened",
    "7z": "el-icon-folder-opened",
    // 其他
    ppt: "el-icon-data-line",
    pptx: "el-icon-data-line"
  }

  return iconMap[extension] || "el-icon-document"
}
