package com.example.demo_oracle_db.exception;

import com.example.demo_oracle_db.util.MessageCode;
import lombok.Getter;

@Getter
public class DodException extends Exception {
    private final String code;

    private final String message;

    public DodException(MessageCode messageCode) {
        code = messageCode.getCode();
        message = messageCode.getCode();
    }

    public DodException(String messageCode) {
        code = "Internal Error - 500";
        message = messageCode;
    }

    public DodException(MessageCode messageCode, Object value) {
        code = messageCode.getCode();
        message = messageCode.format(value);
    }
}
