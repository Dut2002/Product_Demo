import { Component, Input, ViewChild } from '@angular/core';
import { CommonService } from '../../../service/common/common.service';
import { PermissionDto } from '../../../model/dto/permission-dto';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { HttpParams } from '@angular/common/http';
import { SearchBoxDto } from '../../../model/dto/search-box-dto';
import { NgModel } from '@angular/forms';
import { ConfirmModalComponent } from '../../common/confirm-modal/confirm-modal.component';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-role-function-details',
  templateUrl: './role-function-details.component.html',
  styleUrl: './role-function-details.component.scss'
})
export class RoleFunctionDetailsComponent {

  @Input() common!: CommonService;
  @Input() funcId!: number;
  @Input() roleId!: number;
  @Input() permissions: PermissionDto[] = []

  showAdd = false;
  permissionSearch: SearchBoxDto[] = []
  permissionAddId!: number | null
  permissionDeleteId!: number | null

  @ViewChild('confirmModal') confirmModal!: ConfirmModalComponent;


  loadData(){
    const endpoint = this.common.getPermission(PermissionName.UserPermisson.VIEW_USER_FUNCTION_PERMISSION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams();
    params = params.set("roleId", this.roleId);
    params = params.set("functionId", this.funcId);
    this.common.base.get(endpoint, params).subscribe({
      next: res => {
        this.permissions = res

      },
      error: err => this.common.errorHandle.handle(err)
    })
  }

  loadPermissionSearch() {
    const endpoint = this.common.getPermission(PermissionName.UserPermisson.GET_PERMISSION_SEARCH)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams();
    params = params.set("roleId", this.roleId)
    params = params.set("functionId", this.funcId)
    this.common.base.get(endpoint, params).subscribe({
      next: res => this.permissionSearch = res,
      error: err => this.common.errorHandle.handle(err)
    });
  }

  addPermission(permission:NgModel){
    if(permission.valid){
      const endpoint = this.common.getPermission(PermissionName.UserPermisson.ADD_PERMISSION_FOR_USER)
      if (!endpoint) {
        this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        return;
      }
      const body = {
        permissionId: this.permissionAddId,
        roleId: this.roleId
      }
      this.common.base.post(endpoint, body).subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000);
          this.loadData();
          this.permissionAddId = null
        },
        error: err => this.common.errorHandle.handle(err)
      })
    }else{
      this.common.errorHandle.show("Invalid", "Choose a Permission to Add")
    }
  }

  DeletePermission() {
    this.confirmModal.isLoading = true;
    const endpoint = this.common.getPermission(PermissionName.UserPermisson.DELETE_PERMISSION_OF_USER)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      this.confirmModal.isLoading = false;
      return;
    }
    const body = {
      roleId: this.roleId,
      permissionId: this.permissionDeleteId
    }

    this.common.base.deleteBody(endpoint, body)
      .pipe(finalize(() => {
        this.confirmModal.isLoading = false;
      }))
      .subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000);
          this.confirmModal.close();
          this.permissions = this.permissions.filter(p => p.id !== this.permissionDeleteId);
          this.permissionDeleteId = null;
        },
        error: err => this.common.errorHandle.handle(err)
      })
  }

  ConfirmDelete(permissionId: number) {
    this.permissionDeleteId = permissionId;
    console.log(permissionId);

    this.confirmModal.showConfirmation = true;
  }
}
