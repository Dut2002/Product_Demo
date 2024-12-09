package com.example.demo_oracle_db.service.notify.req;

import com.example.demo_oracle_db.util.Constants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifyRequest {

    private Constants.PageRedirect pageRedirect;
    private Integer pageNum;
    private Integer pageSize;
}
