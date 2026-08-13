package com.games.common.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlUtil {
    public static void main(String[] args) {
        String inputDir = "C:\\Users\\Andy\\Downloads\\ruoyi";
        String outputFileName = "merged.sql";
        String outputFile = inputDir + "\\" + outputFileName;

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {
            Stream<Path> paths = Files.list(Paths.get(inputDir)); // 只遍历当前目录    ORDER_IS_OTC

            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".sql"))
                    .filter(path -> !path.getFileName().toString().equals(outputFileName)) // 排除输出文件本身
                    .forEach(path -> {
                        try {
                            writer.write("-- 文件: " + path.getFileName());
                            writer.newLine();
                            Files.lines(path).forEach(line -> {
                                try {
                                    writer.write(line);
                                    writer.newLine();
                                } catch (IOException e) {
                                    log.error("写入 SQL 文件内容失败: {}", path, e);
                                }
                            });
                            writer.newLine(); // 文件之间空一行
                        } catch (IOException e) {
                            log.error("合并 SQL 文件失败: {}", path, e);
                        }
                    });

            System.out.println("合并完成，输出文件：" + outputFile);
        } catch (IOException e) {
            log.error("读取 SQL 目录失败: {}", inputDir, e);
        }
    }
}
