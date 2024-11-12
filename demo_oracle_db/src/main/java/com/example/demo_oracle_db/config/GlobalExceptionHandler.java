package com.example.demo_oracle_db.config;

import com.example.demo_oracle_db.config.dto.ErrorDetails;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.exception.EntityValidationException;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.util.Constants;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler  {

//    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
//    public ResponseEntity<String> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
//        ex.printStackTrace();  // In ra toàn bộ stack trace
//        System.out.println("Exception: " + ex.getMessage());
//        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ex.getMessage());
//    }
    @ExceptionHandler(DodException.class)
    public ResponseEntity<?> handleDobException(DodException ex) {
        return ResponseEntity.badRequest().body(new Res(Constants.ApiStatus.ERROR, "Error" ,ex.getMessage()));
    }

    @ExceptionHandler(EntityValidationException.class)
    public ResponseEntity<?> entityValidationException(EntityValidationException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), ex.getDetails(), request.getDescription(false));
        return new ResponseEntity<>(new Res(Constants.ApiStatus.ERROR, "Validation failed", errorDetails), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        // Kiểm tra nguyên nhân gốc của lỗi
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {
            String fieldName = invalidFormatException.getPath().get(0).getFieldName(); // Lấy tên trường gây lỗi
            Class<?> targetType = invalidFormatException.getTargetType(); // Lấy kiểu dữ liệu cần chuyển đổi

            // Tạo thông điệp lỗi tùy chỉnh
            String errorMessage = String.format("Invalid value for field '%s'. Expected a value of type '%s'.",
                    fieldName, targetType.getSimpleName());

            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("error", errorMessage);

            return new ResponseEntity<>(new Res(Constants.ApiStatus.ERROR, "Malformed JSON request", errorDetails), HttpStatus.BAD_REQUEST);
        }

        // Nếu ngoại lệ không phải là InvalidFormatException, trả về thông báo lỗi mặc định
        return new ResponseEntity<>(new Res(Constants.ApiStatus.ERROR, "Malformed JSON request", "Malformed JSON request"), HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.append(fieldName).append(" field error: ").append(errorMessage).append("<br>");
        });
        // Loại bỏ <br> cuối cùng nếu có
        if (!errors.isEmpty()) {
            errors.setLength(errors.length() - 4); // Giảm chiều dài của StringBuilder để loại bỏ 4 ký tự "<br>"
        }
        return new ResponseEntity<>(new Res(Constants.ApiStatus.ERROR, "Validation failed", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String fieldName = ex.getName(); // Tên của field bị lỗi
        String invalidValue = ex.getValue() != null ? ex.getValue().toString() : "null"; // Giá trị gây ra lỗi
        String message = String.format("Invalid value '%s' for field '%s'. Expected type: '%s'.",
                invalidValue, fieldName, Objects.requireNonNull(ex.getRequiredType()).getSimpleName());

        return new ResponseEntity<>(new Res(Constants.ApiStatus.ERROR,"Validation failed" , message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
