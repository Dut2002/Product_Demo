package com.example.demo_oracle_db.exception;

import com.example.demo_oracle_db.util.MessageCode;
import lombok.Getter;

@Getter
public class EntityValidationException extends Exception {
    Object details;

    public EntityValidationException(String message, Object details) {
        super(message);
        this.details = details;
    }

    public Object getDetails() {
        return details;
    }
}
