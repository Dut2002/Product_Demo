package com.example.demo_oracle_db.config.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ErrorDetails {
    private Date timestamp;

    private String message;

    private Object details;

    private String path;
}
