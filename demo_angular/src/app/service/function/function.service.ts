import { Injectable } from '@angular/core';
import { BaseService } from '../base/base.service';
import { Observable } from 'rxjs';
import { ApiUrls } from '../../constant/api.const.urls';
import { HttpParams } from '@angular/common/http';
import { Function } from '../../model/function';
import { AddPermissionDto } from '../../model/dto/add-permission-dto';
import { UpdatePermissionDto } from '../../model/dto/update-permission-dto';

@Injectable({
  providedIn: 'root'
})
export class FunctionService {

  constructor(private baseService: BaseService) { }

  getFunctions(): Observable<any> {
    return this.baseService.get(ApiUrls.Function.VIEW_FUNCTIONS);
  }

  getPermissionDetail(id: number): Observable<any> {
    let params = new HttpParams();
    params = params.set('id', id.toString()); // Cần gán giá trị trả về cho params
    return this.baseService.get(ApiUrls.Function.VIEW_PERMISSION_DETAILS, params);
  }

  getAllPermissionDetail(functionId: number): Observable<any> {
    let params = new HttpParams();
    params = params.set('id', functionId.toString()); // Cần gán giá trị trả về cho params
    return this.baseService.get(ApiUrls.Function.VIEW_ALL_PERMISSION_DETAILS, params);
  }


  addFunction(data: Function): Observable<any> {
    return this.baseService.post(ApiUrls.Function.ADD_FUNCTION, data);
  }


  updateFunction(data: Function): Observable<any> {
    return this.baseService.put(ApiUrls.Function.CHANG_FUNCTION_NAME, data)
  }

  deleteFunction(id: number): Observable<any> {
    let params = new HttpParams();
    params = params.set('id', id.toString()); // Cần gán giá trị trả về cho params
    return this.baseService.delete(ApiUrls.Function.DELETE_FUNCTION, params)
  }

  addPermission(data: AddPermissionDto): Observable<any> {
    return this.baseService.post(ApiUrls.Function.ADD_PERMISION, data);
  }

  updatePermission(data: UpdatePermissionDto): Observable<any> {
    return this.baseService.put(ApiUrls.Function.UPDATE_PERMISSION, data);
  }

  deletePermission(id: number): Observable<any> {
    let params = new HttpParams();
    params = params.set('id', id.toString()); // Cần gán giá trị trả về cho params
    return this.baseService.delete(ApiUrls.Function.DELETE_PERMISSION, params);
  }
}
