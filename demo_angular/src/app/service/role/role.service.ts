import { Injectable } from '@angular/core';
import { BaseService } from '../base/base.service';
import { ApiUrls } from '../../constant/api.const.urls';
import { Observable } from 'rxjs';
import { Role } from '../../model/role';
import { HttpParams } from '@angular/common/http';
import { ChangeRolePermission } from '../../model/dto/change-role-permission';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  constructor(private baseService: BaseService) { }

  getRoles(): Observable<any> {
    return this.baseService.get(ApiUrls.Role.VIEW_ROLES);
  }

  addRole(role: Role): Observable<any> {
    return this.baseService.post(ApiUrls.Role.ADD_ROLE, role);
  }

  updateRole(role: Role): Observable<any> {
    return this.baseService.put(ApiUrls.Role.UPDATE_ROLE, role);
  }

  deleteRole(roleId: number) {
    let params = new HttpParams();
    params = params.set("id", roleId.toString());
    return this.baseService.delete(ApiUrls.Role.DELETE_ROLE, params);
  }

  changeRolePermission(role: ChangeRolePermission) {
    return this.baseService.put(ApiUrls.Role.CHANGE_ROLE_PERMISSION, role);
  }
}
