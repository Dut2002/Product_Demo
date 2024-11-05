package com.example.demo_oracle_db.service.excelParse;

import com.example.demo_oracle_db.service.excelParse.impl.ExcelMapper;
import com.example.demo_oracle_db.service.excelParse.response.ParseOptions;
import com.example.demo_oracle_db.service.excelParse.response.ParseResult;
import com.example.demo_oracle_db.service.excelParse.response.SheetData;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public abstract class ExcelParse<T> {

    @Autowired
    private Validator validator;

    public ParseResult<T> parseExcel(MultipartFile file, ParseOptions options) {
        ParseResult<T> parseResult = new ParseResult<>();
        parseResult.setSuccess(true);
        parseResult.setListData(new ArrayList<>());
        parseResult.setGlobalErrors(new ArrayList<>());
        parseResult.setSheets(new ArrayList<>());

        if (file.isEmpty()) {
            parseResult.setSuccess(false);
            parseResult.getGlobalErrors().add("Không có file để import");
            return parseResult;
        }

        // Kiểm tra định dạng của file
        String contentType = file.getContentType();
        if (!"application/vnd.ms-excel".equals(contentType) &&
                !"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            parseResult.setSuccess(false);
            parseResult.getGlobalErrors().add("File không đúng định dạng Excel");
            return parseResult;
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            List<Sheet> sheetsToProcess;
            if (options.getSheets() != null && !options.getSheets().isEmpty()) {
                sheetsToProcess = options.getSheets().stream()
                        .map(workbook::getSheetAt)
                        .collect(Collectors.toList());
            } else {
                sheetsToProcess = IntStream.range(0, workbook.getNumberOfSheets())
                        .mapToObj(workbook::getSheetAt)
                        .collect(Collectors.toList());
            }

            if (sheetsToProcess.isEmpty()) {
                parseResult.setSuccess(false);
                parseResult.getGlobalErrors().add("Không tìm thấy sheet nào để xử lý");
                return parseResult;
            }

            for (Sheet sheet : sheetsToProcess) {
                SheetData<T> sheetData = parseSheet(sheet, options);
                parseResult.getSheets().add(sheetData);
                if (!sheetData.getErrors().isEmpty()) {
                    parseResult.setSuccess(false);
                }
                parseResult.getListData().addAll(sheetData.getData());
            }
        } catch (IOException e) {
            parseResult.setSuccess(false);
            parseResult.getGlobalErrors().add("Không có file để import");
        }
        return parseResult;
    }

    protected abstract SheetData<T> parseSheet(Sheet sheet, ParseOptions options);

    private List<String> validateProduct(T item) {
        Set<ConstraintViolation<T>> violations = validator.validate(item);
        return violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
    }
}

