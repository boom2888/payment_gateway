package com.games;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelWriter;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ExcelMergeDemo {

    // 定义输出文件路径变量（绝对路径或相对路径都可以）
    private static final String OUTPUT_FILE_PATH = "/output/merged-demo.xlsx";

    public static void main(String[] args) {
        // 模拟数据
        List<List<Object>> rows = CollUtil.newArrayList(
                Arrays.asList("苹果", "红色", 10),
                Arrays.asList("苹果", "红色", 20),
                Arrays.asList("香蕉", "黄色", 30),
                Arrays.asList("香蕉", "绿色", 15),
                Arrays.asList("香蕉", "绿色", 25),
                Arrays.asList("橘子", "橙色", 12)
        );

        // 确保输出目录存在
        File outputFile = new File(OUTPUT_FILE_PATH);
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                System.err.println("无法创建输出目录: " + parentDir.getAbsolutePath());
                return;
            }
        }

        // 创建ExcelWriter（使用指定路径）
        ExcelWriter writer = new ExcelWriter(OUTPUT_FILE_PATH);

        // 写入表头
        writer.writeRow(Arrays.asList("水果", "颜色", "数量"));

        // 写入内容
        writer.write(rows, true);

        // 合并第一列（索引 0）
        mergeSameColumnValues(writer, rows, 0);

        // 关闭writer，写入磁盘
        writer.close();

        System.out.println("导出成功，文件路径：" + outputFile.getAbsolutePath());
    }

    /**
     * 合并相同值的列（仅限第一列）
     */
    public static void mergeSameColumnValues(ExcelWriter writer, List<List<Object>> rows, int colIndex) {
        if (CollUtil.isEmpty(rows)) return;

        Object prevValue = null;
        int startRow = 1; // 数据从第1行开始（第0行为表头）

        for (int i = 0; i < rows.size(); i++) {
            Object currentValue = rows.get(i).get(colIndex);
            if (!Objects.equals(currentValue, prevValue)) {
                int endRow = i;
                if (endRow - startRow >= 1) {
                    writer.merge(startRow, endRow, colIndex, colIndex, prevValue, false);
                }
                startRow = i + 1;
                prevValue = currentValue;
            }
        }

        // 合并最后一段（防止漏掉）
        int endRow = rows.size();
        if (endRow - startRow >= 1) {
            writer.merge(startRow, endRow, colIndex, colIndex, prevValue, false);
        }
    }
}