package com.example.demo_oracle_db.service;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class ExcelLoadService<T> {

    private Class<T> type;

    public static boolean isValidFileExcel(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public List<T> getListFromExcel(String sheetName, InputStream stream) throws IOException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(stream)) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet not found: " + sheetName);
            }

            // Lấy tiêu đề từ hàng đầu tiên
            Row headerRow = sheet.getRow(0);
            int columnCount = headerRow.getPhysicalNumberOfCells();

            // Đọc từng hàng
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                T obj = createInstance();
                for (int colIndex = 0; colIndex < columnCount; colIndex++) {
                    Cell cell = row.getCell(colIndex);
                    Field field = getFieldByIndex(colIndex);
                    if (field != null) {
                        field.setAccessible(true);
                        Object value = getCellValue(cell);
                        field.set(obj, value);
                    }
                }
                list.add(obj);
            }
        }
        return list;
    }

    private T createInstance() {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create instance of " + type.getName(), e);
        }
    }

    private Field getFieldByIndex(int index) {
        Field[] fields = type.getDeclaredFields();
        return (index < fields.length) ? fields[index] : null;
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case FORMULA -> cell.getCellFormula();
            default -> null;
        };
    }
}
