package com.example.demo_oracle_db.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Constants {
    public interface ApiStatus {
        String SUCCESS = "SUCCESS";
        String ERROR = "ERROR";
        String FAILED = "FAILED";
    }

    public enum Status {
        Active, //đang hoạt động
        Inactive,//không hoạt động
        Pending, // chờ duyệt
        Blocked, // bị chăặn
        Deleted, // bị xóa
        Banned // bị cấm, không thể khôi phục
    }
    public interface Role {
        String ADMIN = "ROLE_ADMIN";
        String SYS_ADMIN = "ROLE_STAFF";
        String CUSTOMER = "ROLE_CUSTOMER";
        String SUPPLIER = "ROLE_SUPPLIER";
    }

    public interface TypeImport {

        int ADD  = 0;
        int UPDATE  = 1;
        int DELETE  = 2;

        static Integer getType(Integer type) {
            switch (type) {
                case ADD -> {
                    return ADD;
                }
                case DELETE -> {
                    return DELETE;
                }
                case UPDATE -> {
                    return UPDATE;
                }
                default -> throw new IllegalArgumentException("Unsupported type Import");
            }
        }
    }

    public enum ApprovalType {
        SUPPLIER_REGISTRATION,
        PRODUCT,
        PROMOTION
    }

    @AllArgsConstructor
    @Getter
    public enum ApprovalStatus {
        PENDING("Pending"),
        APPROVED("Approved"),
        REJECTED("Rejected"),
        CANCELED("Canceled");
        private final String status;
    }
}
