import { Injectable } from '@angular/core';
import { ApiUrls, PermissionName } from '../../constant/api.const.urls';
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
}
