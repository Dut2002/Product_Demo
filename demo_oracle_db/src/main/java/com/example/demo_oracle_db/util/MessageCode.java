package com.example.demo_oracle_db.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageCode {

    FUNCTION_NOT_FOUND("Function not found!"),
    FUNCTION_NAME_EXISTS("Function with name [%s] already exists!"),
    FUNCTION_ALREADY_ADD("Function has been added to role!"),
    FUNCTION_FE_ROUTE_EXISTS("Function with front-end route [%s] already exists!"),
    FUNCTION_ROLE_NOT_FOUND("Function of Role not found!"),
    PERMISSION_NOT_FOUND("Permission not found!"),
    PERMISSION_BE_ENDPOINT_EXISTS("Permission with back-end endpoint [%s] already exists!"),
    PERMISSION_NAME_EXISTS("Permission with name [%s] already exists!"),
    PERMISSION_ALREADY_ADD("Permission has been added to role!"),
    ROLE_PERMISSION_NOT_FOUND("Permission of Role not found!"),
    PERMISSION_REQUIRED("Permission is required for functions"),
    ACCOUNT_WITH_ROLE_NOT_FOUND("Account with role not found!"),
    ROLE_NOT_FOUND("Role not found!"),
    USER_NOT_FOUND("User not found!"),
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
    PRODUCT_NAME_EXIST("Product name already exist!"),
    ADD_PRODUCT_FAILED("Add new product failed!"),
    UPDATE_PRODUCT_FAILED("Update product with [%id] failed!"),
    DELETE_PRODUCT_FAILED("Delete product with [%id] failed!"),
    ROLE_NOT_EXIST("Role does not exist!"),
    ROLE_NAME_EXIST("Role with name [%s] already exist!"),
    ADD_ROLE_FAILED("Add new role failed!"),
    UPDATE_ROLE_FAILED("Update role with [%id] failed!"),
    DELETE_ROLE_FAILED("Delete role with [%id] failed!"),
    VOUCHER_CODE_NOT_EXIST("Voucher with code [%s] does not exist!"),
    VOUCHER_NOT_EXIST("Voucher does not exist!"),
    VOUCHER_EXPIRED("Voucher already expired in date [%s]!"),
    VOUCHER_ALREADY_ADD("Voucher already added to product!"),
    ADD_VOUCHER_FAILED("Add voucher to product failed!"),
    DELETE_VOUCHER_FAILED("Delete voucher from product failed!"),
    VOUCHER_PRODUCT_NOT_FOUND("Voucher not found in product"),
    ILLEGAL_EXTENSION("Unsupported file type [%s]"),
    STATUS_NOT_FOUND("Status not found!"),
    CATEGORY_NOT_EXIST("Category not found"),
    SUPPLIER_NOT_EXIST("Supplier not found"),
    PRODUCT_IN_ORDER("Product has in order!"),
    TYPE_IMPORT_INVALID("Type import invalid!"),
    PERMISSION_DEFAULT("Permission is default"),
    JSON_PARSE_ERROR("Error serializing request to JSON"),
    ADD_REQUEST_FAILED("Send request failed!"),
    PROCESS_REQUEST_ERROR("Process request error!"),
    REQUEST_NOT_FOUND("Request not found!"),
    REQUEST_COMPLETED("This request has already been processed."),
    UNABLE_REQUEST("Unable to submit additional requests"),
    REQUESTER_NOT_MATCH("Requester not match with account"),
    SUPPLIER_ALREADY_REGISTER("Supplier already register!"),
    ROLE_ALREADY_IN_USE("Role already add for any account!"),
    ACCOUNT_ONLY_1_ROLE("Role is required for each account, this account only has one"),
    ROLE_PRIORITY_NOT_FOUND("Role priority not found"),
    ACCOUNT_PRIORITY_NOT_FOUND("Account priority not found"),
    FUNCTION_PRIORITY_NOT_FOUND("Function priority not found"),
    ACCOUNT_NOT_PERMISSION("Account does not have sufficient permissions"),
    USER_ALREADY_BLOCKED("User account already blocked"),
    USER_DELETED("User already deleted"),
    USER_ALREADY_BANNED("User already banned"),
    USER_INACTIVE("User can not active now"),
    NOTIFY_NOT_FOUND("Notify not found"),
    NOTIFY_ALREADY_READ("Notify already check read"), NOTIFY_RECEIVER_EMPTY("Notify receiver list is empty"),
    ADD_NOTIFY_FAILED("Send notify failed!"), USER_ROLE_ALREADY_EXIST("User already has role [%]!"), ROLE_USER_DEFAULT("Role USER is default");

    private final String code;

    public String format(Object value) {
        return String.format(code, value);
    }
}
