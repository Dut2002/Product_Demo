import { Injectable } from '@angular/core';
import { BaseService } from '../base/base.service';
import { ApiUrls } from '../../constant/api.const.urls';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  constructor(private baseService: BaseService) { }

  getRoles(): Observable<any>{
    return this.baseService.get(ApiUrls.Role.GET_ROLES);
  }
}
