package com.example.demo_oracle_db.exception;

import com.example.demo_oracle_db.util.MessageCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DodException extends Exception {
    private final String code;

    private final String message;

    private final HttpStatus status;

    public DodException(MessageCode messageCode) {
        code = messageCode.getCode();
        message = messageCode.getCode();
        status = HttpStatus.BAD_REQUEST;
    }

    public DodException(MessageCode messageCode, HttpStatus status) {
        code = messageCode.getCode();
        message = messageCode.getCode();
        this.status = status;
    }

//    public DodException(String messageCode) {
//        code = "Internal Error - 500";
//        message = messageCode;
//    }

    public DodException(MessageCode messageCode, Object value, HttpStatus status) {
        code = messageCode.getCode();
        message = messageCode.format(value);
        this.status = status;
    }

    public DodException(MessageCode messageCode, Object value) {
        code = messageCode.getCode();
        message = messageCode.format(value);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
