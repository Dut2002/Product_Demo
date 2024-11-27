package com.example.demo_oracle_db.service.user.request;

import com.example.demo_oracle_db.util.Constants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilter {
    String username;
    String email;
    String fullName;
    Constants.Status status;
    Long roleId;

    Integer pageNum;
    Integer pageSize;
}
