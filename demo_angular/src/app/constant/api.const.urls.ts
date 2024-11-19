export const env = "http://localhost:8080"


export class ApiHeaders {
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
    SUPPLIER_BOX: '/api/search/get-supplier-box',
    CUSTOMER_BOX: '/api/search/get-customer-box',
  }

  public static readonly Authentication = {
    LOG_IN: "/api/login/login",
    CHECK_REFRESH: "/api/login/check-refresh-token",
    LOG_OUT: "/api/login/logout"
  }

  public static readonly File = {
    UPLOAD_FILE: "file/upload",
    DOWNLOAD_FILE: "file/download",
  }
}

export class PermissionName {
  public static readonly UserPermisson = {
    VIEW_USER_PERMISSION: 'View User Permission',
    ADD_FUNCTION_FOR_USER: 'Add Function For User',
    DELETE_FUNCTION_OF_USER: 'Delete Function Of User',
    ADD_PERMISSION_FOR_USER: 'Add Permission For User',
    DELETE_PERMISSION_OF_USER: 'Delete Permission Of User',
    GET_ROLE: 'Get Role',
    GET_FUNCTION_SEARCH: 'Get Function Search',
    GET_PERMISSION_SEARCH: 'Get Permission Search',
    VIEW_USER_FUNCTION_PERMISSION: 'View User Function Permission',
  }
  public static readonly UserManagement = {
    VIEW_USERS: 'View Users',
    ADD_USER: 'Add User',
    EDIT_USER: 'Edit User',
    DELETE_USER: 'Delete User',
    CHANGE_USER_STATUS: 'Change User Status',
  }
  public static readonly UserRole = {
    VIEW_ROLES: 'View Roles',
    ADD_ROLE: 'Add Role',
    CHANGE_ROLE_NAME: 'Change Role Name',
    DELETE_ROLE: 'Delete Role',
  }

  public static readonly Shopping = {

  }

  public static readonly ProductManagement = {
    VIEW_PRODUCTS: 'View Products',
    ADD_PRODUCT: 'Add Product',
    UPDATE_PRODUCT: 'Update Product',
    DELETE_PRODUCT: 'Delete Product',
    IMPORT_PRODUCT: 'Import Product',
    UPLOAD_PRODUCT: 'Upload Product',
    EXPORT_PRODUCT: 'Export Product',
    ADD_VOUCHER_FOR_PRODUCT: 'Add Voucher For Product',
    DELETE_VOUCHER_OF_PRODUCT: 'Delete Voucher Of Product',
    VIEW_PRODUCT_VOUCHER_DETAILS: 'View Product Voucher Details',
  }

  public static readonly FunctionManagement = {
    VIEW_FUNCTIONS: 'View Functions',
    ADD_FUNCTION: 'Add Function',
    UPDATE_FUNCTION: 'Update Function',
    DELETE_FUNCTION: 'Delete Function',
    ADD_PERMISSION: 'Add Permission',
    UPDATE_PERMISSION: 'Update Permission',
    DELETE_PERMISSION: 'Delete Permission',
    CHECK_DELETE_PERMISSION: 'Check Delete Permission',
    GET_PERMISSIONS: 'Get Permissions',
  }

  public static readonly OrderManagement = {
    VIEW_ORDER: 'View Order Management',
  }
  public static readonly SupplierManagement = {
    VIEW_SUPPLIER: 'View Supplier Management',
    ADD_SUPPLIER: 'Add Supplier',
    UPDATE_SUPPLIER: 'Update Supplier',
    DELETE_SUPPLIER: 'Delete Supplier',
  }
  public static readonly SupplierApproval = {
    VIEW_REQUEST: 'View Request',
    PROCESS_REQUEST: 'Process Request',
    SAVE_NOTE: 'Save Note',
  }
  public static readonly CustmomerManagement = {
    VIEW_ORDER: 'View Order Management',
  }
  public static readonly SupplierInfomation = {
    VIEW_SUPPLIER_INFO: 'View Supplier Information',
    MY_SUPPLIER_REQUEST: 'My Supplier Request',
    CANCEL_REQUEST: 'Cancel Request',
    SUPPLIER_REGISTER: 'Supplier Register',
  }
}




export class ApiStatus {
  public static readonly SUCCESS = 'success'
  public static readonly ERROR = 'error'
  public static readonly NORMAL = 'normal'
}
