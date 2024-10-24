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
    VIEW_PRODUCTS: "product/get-products",
    GET_PRODUCT: "product/get-product",
    ADD_PRODUCT: "product/add-product",
    UPDATE_PRODUCT: "product/update-product",
    DELETE_PRODUCT: "product/delete-product",
    IMPORT_PRODUCTS: "product/import-products",
    EXPORT_PRODUCTS: "product/export-products",
    ADD_VOUCHER_PRODUCT: "product/add-voucher",
    DELETE_VOUCHER_PRODUCT: "product/delete-voucher",
  }

  public static readonly Vourcher = {
    VOURCHER_DETAILS: 'vourcher/get-detail',
  }

  public static readonly Search = {
    CATEGORY_BOX: "search-box/get-category-box",
    SUPPLIER_BOX : 'search-box/get-supplier-box',
    CUSTOMER_BOX : 'search-box/get-customer-box',
  }

  public static readonly Authentication = {
    LOG_IN: "login/login",
    CHECK_REFRESH: "login/check-refresh-token",
    LOG_OUT: "login/logout"
  }

  public static readonly Function = {
    VIEW_FUNCTIONS: 'function/get-functions',
    VIEW_FUNCTION_PERMISSION: 'function/get-function-permission',
    VIEW_ALL_PERMISSION_DETAILS: 'function/get-all-permission-details',
    VIEW_PERMISSION_DETAILS: 'function/get-permission-details',
    ADD_FUNCTION: 'function/add-function',
    ADD_PERMISION: 'function/add-permission',
    CHANG_FUNCTION_NAME: 'function/update-function',
    UPDATE_PERMISSION: 'function/update-permission',
    DELETE_FUNCTION: 'function/delete-function',
    DELETE_PERMISSION: 'function/delete-permission',
  }

  public static readonly Role = {
    VIEW_ROLES: 'role/get-roles',
    VIEW_ROLE_PERMISSION: 'role/get-role-permission',
    ADD_ROLE: 'role/add-role',
    UPDATE_ROLE: 'role/update-role',
    DELETE_ROLE: 'role/delete-role',
    CHANGE_ROLE_PERMISSION: 'role/change-permission',
  }

  public static readonly   File = {
    UPLOAD_FILE: "file/upload",
    DOWNLOAD_FILE: "file/download",
  }
}

export class ApiStatus {
  public static readonly SUCCESS = 'success'
  public static readonly ERROR = 'error'
  public static readonly NORMAL = 'normal'
}
