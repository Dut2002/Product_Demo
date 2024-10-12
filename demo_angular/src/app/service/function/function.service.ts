import { Injectable } from '@angular/core';
import { BaseService } from '../base/base.service';
import { Observable } from 'rxjs';
import { ApiUrls } from '../../constant/api.const.urls';
import { FunctionDto } from '../../model/dto/function-dto';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FunctionService {

  constructor(private baseService: BaseService) { }

  getFunctions():Observable<any>{
    return this.baseService.get(ApiUrls.Function.GET_FUNCTIONS);
  }

getFunctionDetail(id: number): Observable<any> {
    let params = new HttpParams();
    params = params.set('id', id.toString()); // Cần gán giá trị trả về cho params
    return this.baseService.get(ApiUrls.Function.GET_FUNCTION_DETAIL, params);
}


  addFunction(data: FunctionDto):Observable<any>{
    return this.baseService.post(ApiUrls.Function.ADD_FUNCTION, data);
  }
}
