package com.example.demo_oracle_db.service.function.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteConfirm {
    Long permissionId;
    String message;
    boolean needConfirm;
}
