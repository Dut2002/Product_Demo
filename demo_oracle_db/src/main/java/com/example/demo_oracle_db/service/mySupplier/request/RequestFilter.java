package com.example.demo_oracle_db.service.mySupplier.request;

import com.example.demo_oracle_db.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class RequestFilter {
    Constants.ApprovalType approvalType;
    Constants.ApprovalStatus status;
    LocalDate createdStart;
    LocalDate createdEnd;
    LocalDate updatedStart;
    LocalDate updatedEnd;
    String note;
    Long requesterId;

    Integer pageNum;
    Integer pageSize;
}
