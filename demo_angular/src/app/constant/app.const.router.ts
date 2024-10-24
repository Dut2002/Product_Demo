export class RouterUrl {

  static readonly HOME = 'home/index';
  static readonly NOT_FOUND = 'error/not-found';
  static readonly FORBIDDEN = 'error/forbidden';
  static readonly ERROR = 'error/error';
  static readonly FILE_PROGRESS = 'file/progress';

  static readonly LOG_IN = 'login/login';
  static readonly REGISTER = 'login/register';

  static readonly VIEW_PRODUCTS = 'product/get-products';
  static readonly GET_PRODUCTS = 'product/get-product';
  static readonly ADD_PRODUCT = 'product/add-product';
  static readonly UPDATE_PRODUCT = 'product/update-product';
  static readonly DELETE_PRODUCT = 'product/delete-product';
  static readonly IMPORT_PRODUCTS = 'product/get-products';
  static readonly EXPORT_PRODUCTS = 'product/get-products';
  static readonly ADD_VOUCHER_PRODUCT = 'product/add-voucher';
  static readonly DELETE_VOUCHER_PRODUCT = 'product/delete-voucher';
  static readonly IMPORT_PRODUCT = 'product/import-products';
  static readonly EXPORT_PRODUCT = 'product/export-products';

  static readonly VOURCHER_DETAILS = 'vourcher/get-detail';

  static readonly VIEW_ROLES = 'role/get-roles';
  static readonly VIEW_ROLE_PERMISSION = 'role/get-role-permission';
  static readonly ADD_ROLE = 'role/add-role';
  static readonly UPDATE_ROLE = 'role/update-role';
  static readonly DELETE_ROLE = 'role/delete-role';
  static readonly CHANGE_ROLE_PERMISSION = 'role/change-permission';

  static readonly VIEW_FUNCTIONS = 'function/get-functions';
  static readonly VIEW_FUNCTION_PERMISSION = 'function/get-function-permission';
  static readonly VIEW_PERMISSION_DETAILS = 'function/get-permission-details';
  static readonly ADD_FUNCTION = 'function/add-function';
  static readonly ADD_PERMISION = 'function/add-permission';
  static readonly CHANG_FUNCTION_NAME = 'function/update-function';
  static readonly VOURCHER_UPDATE_PERMISSION = 'functions/update-permission';
  static readonly DELETE_FUNCTION = 'function/delete-function';
  static readonly DELETE_PERMISSION = 'function/delete-permission';

  static readonly CATEGORY_BOX = 'search-box/get-category-box';
  static readonly SUPPLIER_BOX = 'search-box/get-supplier-box';
  static readonly CUSTOMER_BOX = 'search-box/get-customer-box';
}

