package com.games.common.utils.poi;

import com.games.common.annotation.Excel;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;

/**
 * Excel流式读取工具类
 * 用于处理大文件，避免内存溢出
 *
 * @author Kiro
 */
public class ExcelStreamReader<T> {
    
    private static final Logger log = LoggerFactory.getLogger(ExcelStreamReader.class);
    
    private final Class<T> clazz;
    private final List<Field> excelFields;
    private final int batchSize;
    
    public ExcelStreamReader(Class<T> clazz) {
        this(clazz, 1000);
    }
    
    public ExcelStreamReader(Class<T> clazz, int batchSize) {
        this.clazz = clazz;
        this.batchSize = batchSize;
        this.excelFields = getExcelFields();
    }
    
    /**
     * 获取带有@Excel注解的字段
     */
    private List<Field> getExcelFields() {
        List<Field> fields = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        
        for (Field field : declaredFields) {
            Excel excel = field.getAnnotation(Excel.class);
            if (excel != null && (excel.type() == Excel.Type.ALL || excel.type() == Excel.Type.IMPORT)) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        
        // 按照sort排序，如果sort相同则按声明顺序
        fields.sort((f1, f2) -> {
            Excel e1 = f1.getAnnotation(Excel.class);
            Excel e2 = f2.getAnnotation(Excel.class);
            int sortCompare = Integer.compare(e1.sort(), e2.sort());
            if (sortCompare != 0) {
                return sortCompare;
            }
            // sort相同时，保持声明顺序
            return 0;
        });
        
        return fields;
    }
    
    /**
     * 流式读取Excel文件
     *
     * @param inputStream Excel文件输入流
     * @param consumer 批量处理回调函数
     * @return 总行数
     */
    public int readExcel(InputStream inputStream, Consumer<List<T>> consumer) throws Exception {
        OPCPackage opcPackage = null;
        try {
            opcPackage = OPCPackage.open(inputStream);
            XSSFReader reader = new XSSFReader(opcPackage);
            SharedStrings sst = reader.getSharedStringsTable();
            
            XMLReader parser = XMLReaderFactory.createXMLReader();
            SheetHandler handler = new SheetHandler(sst, consumer);
            parser.setContentHandler(handler);
            
            // 读取第一个sheet
            Iterator<InputStream> sheets = reader.getSheetsData();
            if (sheets.hasNext()) {
                InputStream sheet = sheets.next();
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
                sheet.close();
            }
            
            // 处理最后一批数据
            handler.flushBatch();
            
            return handler.getTotalRows();
        } finally {
            if (opcPackage != null) {
                opcPackage.close();
            }
        }
    }
    
    /**
     * SAX处理器
     */
    private class SheetHandler extends DefaultHandler {
        private final SharedStrings sst;
        private final Consumer<List<T>> consumer;
        private final DataFormatter formatter = new DataFormatter();
        
        private String lastContents;
        private boolean nextIsString;
        private int currentRow = 0;
        private int currentCol = 0;
        private int currentColumnIndex = 0;
        private List<String> currentRowData = new ArrayList<>();
        private List<T> batch = new ArrayList<>();
        private int totalRows = 0;
        private boolean isFirstRow = true;
        
        public SheetHandler(SharedStrings sst, Consumer<List<T>> consumer) {
            this.sst = sst;
            this.consumer = consumer;
        }
        
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("row".equals(qName)) {
                String rowNum = attributes.getValue("r");
                if (rowNum != null) {
                    currentRow = Integer.parseInt(rowNum);
                }
                currentRowData.clear();
                currentCol = 0;
            } else if ("c".equals(qName)) {
                // 单元格
                String cellType = attributes.getValue("t");
                String cellRef = attributes.getValue("r");
                currentColumnIndex = parseColumnIndex(cellRef);
                nextIsString = "s".equals(cellType);
                lastContents = "";
            }
        }
        
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("v".equals(qName) || "t".equals(qName)) {
                // 单元格值
                if (nextIsString) {
                    try {
                        int idx = Integer.parseInt(lastContents);
                        lastContents = sst.getItemAt(idx).getString();
                    } catch (Exception e) {
                        log.error("解析共享字符串失败", e);
                    }
                }
                ensureColumnCapacity(currentRowData, currentColumnIndex);
                currentRowData.set(currentColumnIndex, lastContents);
                currentCol++;
            } else if ("row".equals(qName)) {
                // 行结束
                if (currentRow == 1) {
                    // 跳过标题行
                    isFirstRow = false;
                } else if (!isFirstRow && !isEmptyRow(currentRowData)) {
                    try {
                        T obj = parseRow(currentRowData);
                        if (obj != null) {
                            batch.add(obj);
                            totalRows++;
                            
                            // 达到批次大小，执行回调
                            if (batch.size() >= batchSize) {
                                consumer.accept(new ArrayList<>(batch));
                                batch.clear();
                            }
                        }
                    } catch (Exception e) {
                        log.error("解析第{}行数据失败: {}", currentRow, e.getMessage());
                    }
                }
            }
        }
        
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            lastContents += new String(ch, start, length);
        }
        
        private boolean isEmptyRow(List<String> rowData) {
            return rowData.stream().allMatch(s -> s == null || s.trim().isEmpty());
        }

        private void ensureColumnCapacity(List<String> rowData, int columnIndex) {
            while (rowData.size() <= columnIndex) {
                rowData.add("");
            }
        }

        private int parseColumnIndex(String cellRef) {
            if (cellRef == null) {
                return currentCol;
            }
            int col = 0;
            for (int i = 0; i < cellRef.length(); i++) {
                char ch = cellRef.charAt(i);
                if (Character.isLetter(ch)) {
                    col = col * 26 + (Character.toUpperCase(ch) - 'A' + 1);
                } else {
                    break;
                }
            }
            return Math.max(col - 1, currentCol);
        }
        
        private T parseRow(List<String> rowData) throws Exception {
            T obj = clazz.getDeclaredConstructor().newInstance();
            
            for (int i = 0; i < excelFields.size() && i < rowData.size(); i++) {
                Field field = excelFields.get(i);
                String value = i < rowData.size() ? rowData.get(i) : "";
                
                if (value != null && !value.trim().isEmpty()) {
                    setFieldValue(obj, field, value.trim());
                }
            }
            
            return obj;
        }
        
        private void setFieldValue(T obj, Field field, String value) throws Exception {
            Class<?> fieldType = field.getType();
            
            try {
                if (fieldType == String.class) {
                    field.set(obj, value);
                } else if (fieldType == Integer.class || fieldType == int.class) {
                    field.set(obj, Integer.parseInt(value));
                } else if (fieldType == Long.class || fieldType == long.class) {
                    field.set(obj, Long.parseLong(value));
                } else if (fieldType == Double.class || fieldType == double.class) {
                    field.set(obj, Double.parseDouble(value));
                } else if (fieldType == BigDecimal.class) {
                    field.set(obj, new BigDecimal(value));
                } else if (fieldType == Date.class) {
                    // 简单的日期处理，可以根据需要扩展
                    field.set(obj, new Date());
                }
            } catch (Exception e) {
                log.warn("设置字段{}值失败: {}", field.getName(), e.getMessage());
            }
        }
        
        public void flushBatch() {
            if (!batch.isEmpty()) {
                consumer.accept(new ArrayList<>(batch));
                batch.clear();
            }
        }
        
        public int getTotalRows() {
            return totalRows;
        }
    }
}
