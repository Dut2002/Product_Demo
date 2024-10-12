package com.example.demo_oracle_db.service.function.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFunctionReq {
    Long id;
    String name;
    String endPoint;
}
