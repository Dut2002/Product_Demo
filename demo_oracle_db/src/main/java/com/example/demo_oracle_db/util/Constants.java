package com.example.demo_oracle_db.util;

public class Constants {
    public interface ApiStatus {
        String SUCCESS = "SUCCESS";
        String ERROR = "ERROR";
        String FAILED = "FAILED";
    }

    public interface Status {
        String ACTIVE = "ACTIVE";
    }
    public interface Role {

        String USER = "ROLE_USER";
        String ADMIN = "ROLE_ADMIN";
        String SYS_ADMIN = "ROLE_SYS_ADMIN";
        String CUSTOMER = "ROLE_CUSTOMER";
    }
}
