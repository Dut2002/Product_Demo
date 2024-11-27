package com.example.demo_oracle_db.service.user.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteRoleUserReq {
    Long accountId;
    Long roleId;
}
