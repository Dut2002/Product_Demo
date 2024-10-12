import { Injectable } from '@angular/core';
import { ApiUrls } from '../../constant/api.const.urls';
import { BaseService } from '../base/base.service';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../../model/product';
import { ProductFilter } from '../../model/dto/product-filter';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private baseService: BaseService) { }

  getProducts(
    productFilter: ProductFilter
  ): Observable<any> {
    const body = { ...productFilter
    };

    return this.baseService.get_body(ApiUrls.Product.GET_PRODUCTS, body);
  }

  addProduct(product:Product){
    return this.baseService.post(ApiUrls.Product.ADD_PRODUCT, product);
  }

  updateProduct(product:Product){
    return this.baseService.put(ApiUrls.Product.UPDATE_PRODUCT, product);
  }

  deleteProduct(id:number){
    let params = new HttpParams()
    params = params.set('id', id)
    return this.baseService.delete(ApiUrls.Product.DELETE_PRODUCT, params)
  }

  getCategoryBox(name: string|null){
    let params = new HttpParams()
    if(name) params = params.set('name', name)
    return this.baseService.get(ApiUrls.Product.GET_CATEGORY_BOX, params)
  }

  getSupplierBox(name: string|null){
    let params = new HttpParams()
    if(name) params = params.set('name', name)
    return this.baseService.delete(ApiUrls.Product.GET_SUPPLIER_BOX, params)
  }

  getCustomerBox(name: string|null){
    let params = new HttpParams()
    if(name) params = params.set('name', name)
    return this.baseService.delete(ApiUrls.Product.GET_CATEGORY_BOX, params)
  }
}
