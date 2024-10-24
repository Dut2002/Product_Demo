import { Injectable } from '@angular/core';
import { ApiUrls } from '../../constant/api.const.urls';
import { BaseService } from '../base/base.service';
import { HttpEvent, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../../model/product';
import { ProductFilter } from '../../model/dto/product-filter';
import { AddVoucherRequet } from '../../model/dto/add-voucher-request';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private baseService: BaseService) { }

  getProducts(
    productFilter: ProductFilter
  ): Observable<any> {
    const body = {
      ...productFilter
    };

    return this.baseService.get_body(ApiUrls.Product.VIEW_PRODUCTS, body);
  }

  addProduct(product: Product) {
    return this.baseService.post(ApiUrls.Product.ADD_PRODUCT, product);
  }

  updateProduct(product: Product) {
    return this.baseService.put(ApiUrls.Product.UPDATE_PRODUCT, product);
  }

  deleteProduct(id: number) {
    let params = new HttpParams()
    params = params.set('id', id)
    return this.baseService.delete(ApiUrls.Product.DELETE_PRODUCT, params)
  }

  getCategoryBox(name: string | null) {
    let params = new HttpParams()
    if (name) params = params.set('name', name)
    return this.baseService.get(ApiUrls.Search.CATEGORY_BOX, params)
  }

  getSupplierBox(name: string | null) {
    let params = new HttpParams()
    if (name) params = params.set('name', name)
    return this.baseService.get(ApiUrls.Search.SUPPLIER_BOX, params)
  }

  getCustomerBox(name: string | null) {
    let params = new HttpParams()
    if (name) params = params.set('name', name)
    return this.baseService.get(ApiUrls.Search.CUSTOMER_BOX, params)
  }

  addVoucher(productId: number, code: string) {
    const body: AddVoucherRequet =
    {
      productId: productId, voucherCode: code
    }

    return this.baseService.post(ApiUrls.Product.ADD_VOUCHER_PRODUCT, body);
  }

  deleteVoucher(productId: number, voucherId: number){
    let params = new HttpParams()
    params = params.set('productId', productId)
    params = params.set('voucherId', voucherId)
    return this.baseService.delete(ApiUrls.Product.DELETE_VOUCHER_PRODUCT, params)
  }

  exportProduct(option: string): Observable<HttpResponse<Blob>>{
    let params = new HttpParams()
    params = params.set('option', option);
    return this.baseService.export(ApiUrls.Product.EXPORT_PRODUCTS, params)
  }
}
