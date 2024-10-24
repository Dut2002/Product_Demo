package com.example.demo_oracle_db.service.user.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilter {
    String username;
    String email;
    String fullName;
    String status;
    Long roleId;

    Integer pageNum;
    Integer pageSize;
}
