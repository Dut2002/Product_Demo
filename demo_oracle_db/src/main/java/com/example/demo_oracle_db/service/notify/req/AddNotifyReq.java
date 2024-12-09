package com.example.demo_oracle_db.service.notify.req;

import com.example.demo_oracle_db.util.Constants;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AddNotifyReq {

    private String header;

    private String message;

    private LocalDateTime timestamp;

    private Constants.PageRedirect pageRedirect;

    private String data;
}
