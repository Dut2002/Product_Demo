package com.example.demo_oracle_db.service.function.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePermissionRequest {
    Long id;
    String name;
    String beEndPoint;
}
