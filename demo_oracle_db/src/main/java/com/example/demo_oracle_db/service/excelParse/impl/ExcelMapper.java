package com.example.demo_oracle_db.service.excelParse.impl;

import com.example.demo_oracle_db.service.excelParse.response.HeaderMapping;
import com.example.demo_oracle_db.service.excelParse.response.MapResult;
import com.example.demo_oracle_db.service.excelParse.response.ParseOptions;
import com.example.demo_oracle_db.service.excelParse.response.SheetData;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

@AllArgsConstructor
public abstract class ExcelMapper<T> {
    private final Class<T> targetClass;
    private final List<HeaderMapping<T>> mappings;


    public SheetData<T> mapFromExcelSheet(Sheet sheet, ParseOptions options) {
        SheetData<T> sheetData = new SheetData<>();
        sheetData.setSheetName(sheet.getSheetName());
        sheetData.setErrors(new ArrayList<>());

        List<T> results = new ArrayList<>();
        Map<String, Set<Object>> uniqueValuesMap = new HashMap<>();
        Map<String, Integer> headerIndexMap = getHeaderIndexMap(sheet.getRow(options.getHeaderRow() - 1));
        List<String> missingColumns = validateRequiredColumns(headerIndexMap, options);
        if (!missingColumns.isEmpty()) {
            for (String missingColumn : missingColumns
            ) {
                sheetData.getErrors().add("Missing required columns: " + missingColumn);
            }
            return sheetData;
        }
        for (int i = options.getHeaderRow(); i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            MapResult<T> mapResult = mapRow(row, headerIndexMap, uniqueValuesMap, options);
            if (!mapResult.getErrors().isEmpty()) {
                sheetData.getErrors().addAll(mapResult.getErrors());
            }
            if(mapResult.getItem()!=null){
                results.add(mapResult.getItem());
            }
        }
        sheetData.setData(results);
        return sheetData;
    }


    private Map<String, Integer> getHeaderIndexMap(Row headerRow) {
        Map<String, Integer> headerIndexMap = new HashMap<>();
        for (Cell cell : headerRow) {
            String headerValue = cell.getStringCellValue().trim();
            headerIndexMap.put(headerValue, cell.getColumnIndex());
        }
        return headerIndexMap;
    }

    protected abstract List<String> validateRequiredColumns(Map<String, Integer> headerIndexMap, ParseOptions options);

    private MapResult<T> mapRow(Row row, Map<String, Integer> headerIndexMap, Map<String, Set<Object>> uniqueValuesMap, ParseOptions options) {
        MapResult<T> mapResult = new MapResult<>();
        mapResult.setErrors(new ArrayList<>());

        try {
            T item = targetClass.getDeclaredConstructor().newInstance();
            boolean validData = true;
            boolean hasData = false;
            List<String> errors = new ArrayList<>();
            for (HeaderMapping<T> mapping : mappings
            ) {
                Integer rowIndex = headerIndexMap.get(mapping.getHeaderName());
                if (rowIndex != null) {
                    try {
                        Cell cell = row.getCell(rowIndex);
                        Object value = mapping.getConverter().apply(cell);
                        if (value != null) {
                            hasData = true;
                        } else if (mapping.getRequired().get(options.getTypeImport())) {
                            validData = false;
                            errors.add(
                                    String.format("Giá trị của cột '%s' dòng '%s' bị để trống", mapping.getHeaderName(), row.getRowNum()+1)
                            );
                            continue;
                        }
                        mapping.getSetter().accept(item, value);
                        if (mapping.getUnique().get(options.getTypeImport()) && value != null) {
                            uniqueValuesMap.computeIfAbsent(mapping.getHeaderName(), k -> new HashSet<>());
                            Set<Object> uniqueValues = uniqueValuesMap.get(mapping.getHeaderName());
                            List<String> checkUnique =  checkUnique(value, uniqueValues, mapping, row.getRowNum()+1, options.getTypeImport());
                            if(!checkUnique.isEmpty()){
                                validData = false;
                                errors.addAll(checkUnique);
                            }
                        }
                    } catch (Exception e) {
                        errors.add("Dòng " + (row.getRowNum()+1) + " cột " + mapping.getHeaderName() + " lỗi dữ liệu khi chuyển đổi");
                    }
                }
            }
            if (hasData) {
                errors.addAll(validateItem(item, row.getRowNum() + 1, options.getTypeImport()));
                mapResult.setErrors(errors);
                if (!errors.isEmpty() || !validData) {
                    setFailed(item);
                }
                mapResult.setItem(item);
            }
        } catch (Exception e) {
            mapResult.setErrors(List.of("Dòng " + (row.getRowNum()+1) + " không thể convert data"));
        }
        return mapResult;
    }

    protected abstract void setFailed(T item);

    protected abstract List<String> checkUnique(Object value, Set<Object> uniqueValues, HeaderMapping<T> mapping, int rowNum, Integer typeImport);
    protected abstract List<String> validateItem(T item, int rowNum, Integer typeImport);
}
