package com.example.demo_oracle_db.util;

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
        String ADMIN = "ADMIN";
        String SYS_ADMIN = "STAFF";
        String CUSTOMER = "CUSTOMER";
    }
}
