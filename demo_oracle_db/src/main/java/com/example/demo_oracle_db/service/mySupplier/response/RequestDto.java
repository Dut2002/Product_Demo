package com.example.demo_oracle_db.service.mySupplier.response;

import com.example.demo_oracle_db.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class RequestDto {
    Long id;
    String data;
    Constants.ApprovalType approvalType;
    Constants.ApprovalStatus status;
    Long requesterId;
    String fullName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String note;
}
