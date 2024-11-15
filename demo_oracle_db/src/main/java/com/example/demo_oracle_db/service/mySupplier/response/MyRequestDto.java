package com.example.demo_oracle_db.service.mySupplier.response;

import com.example.demo_oracle_db.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyRequestDto {
    Long id;
    String data;
    Constants.ApprovalStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String note;
}
