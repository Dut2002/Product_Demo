package com.example.demo_oracle_db.service.function.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddFunctionReq {
    Long id;
    String name;
    String endPoint;
    List<RoleAccess> roleAccesses;
}
