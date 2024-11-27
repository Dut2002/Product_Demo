package com.example.demo_oracle_db.service.user.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChangeRoleUserReq {
    Long accountId;
    Long[] roleId;
}
