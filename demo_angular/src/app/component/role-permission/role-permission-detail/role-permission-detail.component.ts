import { HttpParams } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { NgModel } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { RouterUrl } from '../../../constant/app.const.router';
import { addRoleFunc } from '../../../model/dto/add-role-func';
import { RolePermissionDto } from '../../../model/dto/role-permission-dto';
import { SearchBoxDto } from '../../../model/dto/search-box-dto';
import { CommonService } from '../../../service/common/common.service';
import { ConfirmModalComponent } from '../../common/confirm-modal/confirm-modal.component';

@Component({
  selector: 'app-role-permission-detail',
  templateUrl: './role-permission-detail.component.html',
  styleUrl: './role-permission-detail.component.scss'
})
export class RolePermissionDetailComponent {

  @ViewChild('confirmModal') confirmModal!: ConfirmModalComponent;
  functionSearch: SearchBoxDto[] = []
  funcAddId!: number | null
  funcDeleteId!: number | null
  rolePermission?: RolePermissionDto;
  roleId!: number
  constructor(public common: CommonService,
    private route: ActivatedRoute
  ) {
    this.common.setFunctionName(this.route);
    this.route.queryParams.subscribe(params => {
      this.roleId = params['roleId'];
      if (this.roleId) {
        this.loadData();
      } else {
        this.common.router.navigate([RouterUrl.NOT_FOUND]);
      }
    });
  }

  DeleteFunction() {
    this.confirmModal.isLoading = true;
    const endpoint = this.common.getPermission(PermissionName.UserPermisson.DELETE_FUNCTION_OF_USER)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      this.confirmModal.isLoading = false;
      return;
    }
    const body = {
      roleId: this.roleId,
      functionId: this.funcDeleteId
    }

    this.common.base.deleteBody(endpoint, body)
      .pipe(finalize(() => {
        this.confirmModal.isLoading = false;
      }))
      .subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000);
          this.confirmModal.close();
          this.rolePermission!.functions = this.rolePermission!.functions.filter(f => f.id !== this.funcDeleteId);
          this.funcDeleteId = null;
        },
        error: err => this.common.errorHandle.handle(err)
      })
  }

  ConfirmDelete(funcId: number) {
    this.funcDeleteId = funcId;
    this.confirmModal.showConfirmation = true;
  }



  loadData() {
    const endpoint = this.common.getPermission(PermissionName.UserPermisson.VIEW_USER_PERMISSION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams();
    params = params.set("id", this.roleId)
    this.common.base.get(endpoint, params).subscribe({
      next: res => {
        this.rolePermission = res;
      },
      error: err => this.common.errorHandle.handle(err)
    })
  }

  loadFunctionSearch() {
    const endpoint = this.common.getPermission(PermissionName.UserPermisson.GET_FUNCTION_SEARCH)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams();
    params = params.set("id", this.roleId)
    this.common.base.get(endpoint, params).subscribe({
      next: res => this.functionSearch = res,
      error: err => this.common.errorHandle.handle(err)
    });
  }


  AddFunction(func: NgModel) {
    if (func.valid) {
      const endpoint = this.common.getPermission(PermissionName.UserPermisson.ADD_FUNCTION_FOR_USER)
      if (!endpoint) {
        this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        return;
      }
      const body = {
        functionId: this.funcAddId,
        roleId: this.roleId
      }
      this.common.base.post(endpoint, body).subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000);
          this.loadData();
          this.funcAddId = null
        },
        error: err => this.common.errorHandle.handle(err)
      })
    } else {
      this.common.errorHandle.show("Invalid", "Choose a Function to Add")
    }
  }


}
