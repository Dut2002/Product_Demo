package com.example.demo_oracle_db.service.fixSystem.request;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class AddSuperAdminFunction {
    @NotNull
    Long functionId;
}
