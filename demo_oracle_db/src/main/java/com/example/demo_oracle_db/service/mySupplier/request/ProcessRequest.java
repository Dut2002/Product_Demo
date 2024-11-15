package com.example.demo_oracle_db.service.mySupplier.request;

import com.example.demo_oracle_db.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class ProcessRequest {
    @NotNull
    Long id;
    @NotNull
    Constants.ApprovalStatus status;
    String note;
    @NotNull
    Constants.ApprovalType approvalType;
}
