package com.example.demo_oracle_db.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageCode{

    FUNCTION_NOT_FOUND("Function not found!"),
    FUNCTION_NAME_EXISTS("Function with name [%s] already exists!"),
    FUNCTION_ENDPOINT_EXISTS("Function with endpoint [%s] already exists!"),
    ROLE_NOT_FOUND("Role not found!"),
    USER_NAME_NOT_EXIST("User with username [%s] does not exist!"),
    USER_NAME_ALREADY_EXISTS("Username [%s] already exists!"),
    EMAIL_ALREADY_EXISTS("Email [%s] already exists!"),
    PASSWORD_INCORRECT("Password is incorrect!"),
    LOGIN_FAILED(" Login Failed!"),
    REGISTER_USER_FAILED("Register new user failed!"),
    INACTIVE_USER("User with username [%s] is not active!"),
    TOKEN_EXPIRED("Token expire!"),
    TOKEN_INVALID("Token invalid!"),
    TOKEN_NOT_EXISTS("User with username [%s] does not have token to access!"),
    TOKEN_FAILED("Token generates failed!"),
    REFRESH_FAILED("Refresh failed!"),
    USER_UNAUTHORIZED("User is unauthorized to access this resource!"),
    PRODUCT_NOT_EXIST("Product does not exist!"),
    ADD_PRODUCT_FAILED("Add new product failed!"),
    UPDATE_PRODUCT_FAILED("Update product with [%id] failed!"),
    DELETE_PRODUCT_FAILED("Delete product with [%id] failed!"),
    ROLE_NOT_EXIST("Role with id [%s] does not exist!"),
    ROLE_NAME_EXIST("Role with name [%s] already exist!"),
    ADD_ROLE_FAILED("Add new role failed!"),
    UPDATE_ROLE_FAILED("Update role with [%id] failed!"),
    DELETE_ROLE_FAILED("Delete role with [%id] failed!"),
    VOUCHER_CODE_NOT_EXIST("Voucher with code [%s] does not exist!"),
    VOUCHER_EXPIRED("Voucher already expired in date [%s]!"),
    VOUCHER_ALREADY_ADD("Voucher already added to product!"),
    ADD_VOUCHER_FAILED("Add voucher to product failed!"),
    DELETE_VOUCHER_FAILED("Delete voucher from product failed!"),
    VOUCHER_PRODUCT_NOT_FOUND("Voucher not found in product")

            ;

    private final String code;

    public String format(Object value) {
        return String.format(code, value);
    }
}
