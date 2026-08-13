package com.games.common.utils;

import cn.hutool.core.io.IoUtil;
import com.games.common.constant.Constants;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.zip.ZipOutputStream;

public class ExcelZipUtil {

    /**
     * 压缩多个 Excel 文件流为一个 zip 文件（返回字节数组）
     *
     * @param fileMap Map<文件名（含.xlsx扩展名）, 对应的输入流>
     * @throws IOException IO 异常
     */
    public static void zipExcelFiles(Map<String, InputStream> fileMap, ZipOutputStream zip) throws IOException {
        for (Map.Entry<String, InputStream> entry : fileMap.entrySet()) {
            String fileName = entry.getKey();
            InputStream inputStream = entry.getValue();
            try {
                zip.putNextEntry(new java.util.zip.ZipEntry(fileName));
                IOUtils.copy(inputStream, zip);
                zip.flush();
                zip.closeEntry();
            } finally {
                IoUtil.close(inputStream);
            }
        }

//        return zipOutStream.toByteArray();
    }
}
