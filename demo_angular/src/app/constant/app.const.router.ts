export class RouterUrl{
  static readonly BASE_URL = { name: 'BaseUrl', path: 'app/index' };
  static readonly GET_FUNCTIONS = {name: 'GetFunctions', path: 'function/get-functions'};
  static readonly LOG_IN = { name: 'Login', path: 'login/login' };
  static readonly REGISTER = { name: 'Register', path: 'login/register' };
  static readonly GET_PRODUCTS = { name: 'GetProducts', path: 'product/get-products' };
  static readonly ADD_PRODUCT = { name: 'AddProduct', path: 'product/get-products' };
  static readonly UPDATE_PRODUCT = { name: 'UpdateProduct', path: 'product/get-products' };
  static readonly DELETE_PRODUCT = { name: 'DeleteProduct', path: 'product/get-products' };
  static readonly IMPORT_PRODUCTS = { name: 'GetProducts', path: 'product/get-products' };
  static readonly EXPORT_PRODUCTS = { name: 'GetProducts', path: 'product/get-products' };




  static readonly NOT_FOUND = { name: 'NotFound', path: 'error/not-found' };
  static readonly FORBIDDEN = { name: 'Forbidden', path: 'error/forbidden' };
  static readonly ERROR = { name: 'Error', path: 'error/error' };
}
