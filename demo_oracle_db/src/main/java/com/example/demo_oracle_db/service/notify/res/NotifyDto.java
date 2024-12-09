package com.example.demo_oracle_db.service.notify.res;

import com.example.demo_oracle_db.util.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotifyDto {

    private Long id;

    private String header;

    private String message;

    private LocalDateTime timestamp;

    private Constants.PageRedirect pageRedirect;

    private String data;

    private boolean isRead;

    public NotifyDto(Long id, String header, String message, LocalDateTime timestamp, Constants.PageRedirect pageRedirect, String data, Integer isRead) {
        this.id = id;
        this.header = header;
        this.message = message;
        this.timestamp = timestamp;
        this.pageRedirect = pageRedirect;
        this.data = data;
        this.isRead = isRead == 1;
    }
}
