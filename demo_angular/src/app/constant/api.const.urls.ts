export const env = "http://localhost:8080/api/"


export class ApiHeaders{
  public static readonly HEADER_DEFAULT = {
    Accept: "application/json",
    "Content-Type": "application/json",
  };
  public static readonly HEADER_UPLOAD = {
    Accept: "application/json",
    "Content-Type": "multipart/form-data",
  };

  public static readonly TOKEN_KEY = 'token'
  public static readonly REFRESH_KEY = 'refreshToken'
  public static readonly ROLE_KEY = 'role'
  public static readonly AUTHORITY = 'authority'
  public static readonly PREVIOUS = 'previousUrl'
}

export class ApiUrls {
  public static readonly Product = {
    GET_PRODUCTS: "product/get-products",
    GET_PRODUCT: "product/get-product",
    ADD_PRODUCT: "product/add-product",
    UPDATE_PRODUCT: "product/update-product",
    DELETE_PRODUCT: "product/delete-product",
    IMPORT_PRODUCTS: "product/import-products",
    EXPORT_PRODUCTS: "product/export-products",
    GET_CATEGORY_BOX: "product/get-category-box",
    GET_SUPPLIER_BOX: "product/get-supplier-box",
    GET_CUSTOMER_BOX: "product/get-customer-box",

  }

  public static readonly Authentication = {
    LOG_IN: "login/login",
    CHECK_REFRESH: "login/check-refresh-token",
    LOG_OUT: "login/logout"
  }

  public static readonly Function = {
    GET_FUNCTIONS: "function/get-functions",
    GET_FUNCTION_DETAIL: "function/get-function-detail",
    ADD_FUNCTION: "function/add-function",
    UPDATE_FUNCTION: "function/update-function",
    CHANGE_ACCESS: "function/modify-function-access",
    DELETE_FUNCTION: "function/delete-function",
  }

  public static readonly Role = {
    GET_ROLES: "role/get-roles",
    GET_BY_ID: "role/get-role-by-id",
    ADD_ROLE: "role/add-role",
    UPDATE_ROLE: "role/update-role",
    GET_BY_NAME: "role/get-role-by-name",
    DELETE_ROLE: "role/delete-role",
  }
}

export class ApiStatus {
  public static readonly SUCCESS = 'success'
  public static readonly ERROR = 'error'
  public static readonly NORMAL = 'normal'
}
