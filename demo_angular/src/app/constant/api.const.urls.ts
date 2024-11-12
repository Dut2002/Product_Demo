export const env = "http://localhost:8080"


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
  public static readonly ROLE_KEY = 'roles'
  public static readonly PREVIOUS = 'previousUrl'
}

export class ApiUrls {

  public static readonly Search = {
    CATEGORY_BOX: "/api/search/get-category-box",
    SUPPLIER_BOX : '/api/search/get-supplier-box',
    CUSTOMER_BOX : '/api/search/get-customer-box',
  }

  public static readonly Authentication = {
    LOG_IN: "/api/login/login",
    CHECK_REFRESH: "/api/login/check-refresh-token",
    LOG_OUT: "/api/login/logout"
  }

  public static readonly   File = {
    UPLOAD_FILE: "file/upload",
    DOWNLOAD_FILE: "file/download",
  }
}

export class PermissionName {
  public static readonly VIEW_USER_PERMISSION = 'View User Permission';
  public static readonly ADD_FUNCTION_FOR_USER = 'Add Function For User';
  public static readonly DELETE_FUNCTION_OF_USER = 'Delete Function Of User';
  public static readonly ADD_PERMISSION_FOR_USER = 'Add Permission For User';
  public static readonly DELETE_PERMISSION_OF_USER = 'Delete Permission Of User';
  public static readonly VIEW_USERS = 'View Users';
  public static readonly ADD_USER = 'Add User';
  public static readonly EDIT_USER = 'Edit User';
  public static readonly DELETE_USER = 'Delete User';
  public static readonly CHANGE_USER_STATUS = 'Change User Status';
  public static readonly VIEW_ROLES = 'View Roles';
  public static readonly ADD_ROLE = 'Add Role';
  public static readonly CHANGE_ROLE_NAME = 'Change Role Name';
  public static readonly DELETE_ROLE = 'Delete Role';
  public static readonly VIEW_PRODUCTS = 'View Products';
  public static readonly ADD_PRODUCT = 'Add Product';
  public static readonly UPDATE_PRODUCT = 'Update Product';
  public static readonly DELETE_PRODUCT = 'Delete Product';
  public static readonly IMPORT_PRODUCT = 'Import Product';
  public static readonly UPLOAD_PRODUCT = 'Upload Product';
  public static readonly EXPORT_PRODUCT = 'Export Product';
  public static readonly ADD_VOUCHER_FOR_PRODUCT = 'Add Voucher For Product';
  public static readonly DELETE_VOUCHER_OF_PRODUCT = 'Delete Voucher Of Product';
  public static readonly VIEW_PRODUCT_VOUCHER_DETAILS = 'View Product Voucher Details';
  public static readonly VIEW_FUNCTIONS = 'View Functions';
  public static readonly ADD_FUNCTION = 'Add Function';
  public static readonly UPDATE_FUNCTION = 'Update Function';
  public static readonly DELETE_FUNCTION = 'Delete Function';
  public static readonly ADD_PERMISSION = 'Add Permission';
  public static readonly UPDATE_PERMISSION = 'Update Permission';
  public static readonly DELETE_PERMISSION = 'Delete Permission';
  public static readonly CHECK_DELETE_PERMISSION = 'Check Delete Permission'
  public static readonly GET_PERMISSIONS = 'Get Permissions';
}


export class ApiStatus {
  public static readonly SUCCESS = 'success'
  public static readonly ERROR = 'error'
  public static readonly NORMAL = 'normal'
}
