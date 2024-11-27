import { Component, Input, ViewChild } from '@angular/core';
import { finalize } from 'rxjs';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { NgModel } from '@angular/forms';
import { HttpParams } from '@angular/common/http';
import { CommonService } from '../../../service/common/common.service';
import { PermissionDto } from '../../../model/dto/permission-dto';
import { SearchBoxDto } from '../../../model/dto/search-box-dto';
import { ConfirmModalComponent } from '../../common/confirm-modal/confirm-modal.component';
import { AddPermissionDto } from '../../../model/dto/add-permission-dto';
import { PermissionAddComponent } from '../../function/permission-add/permission-add.component';

@Component({
  selector: 'app-fix-system-details',
  templateUrl: './fix-system-details.component.html',
  styleUrl: './fix-system-details.component.scss'
})
export class FixSystemDetailsComponent {
  @Input() common!: CommonService;
  @Input() funcId!: number;
  @Input() permissions: PermissionDto[] = []

  showAdd = false;
  permissionSearch: SearchBoxDto[] = []
  permissionAddId!: number | null
  permissionDeleteId!: number | null

  @ViewChild('confirmModal') confirmModal!: ConfirmModalComponent;
  @ViewChild('permissionAdd') permissionAdd!: PermissionAddComponent;



  loadData(){
    const endpoint = this.common.getPermission(PermissionName.FixSystem.GET_FUNCTION_PERMISSION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams();
    params = params.set("functionId", this.funcId);
    this.common.base.get(endpoint, params).subscribe({
      next: res => {
        this.permissions = res

      },
      error: err => this.common.errorHandle.handle(err)
    })
  }

  loadPermissionSearch() {
    const endpoint = this.common.getPermission(PermissionName.FixSystem.GET_PERMISSION_SEARCH)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams();
    params = params.set("functionId", this.funcId)
    this.common.base.get(endpoint, params).subscribe({
      next: res => this.permissionSearch = res,
      error: err => this.common.errorHandle.handle(err)
    });
  }

  addPermission(permission:NgModel){
    if(permission.valid){
      const endpoint = this.common.getPermission(PermissionName.FixSystem.ADD_PERMISSION)
      if (!endpoint) {
        this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        return;
      }
      const body = {
        permissionId: this.permissionAddId,
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
    const endpoint = this.common.getPermission(PermissionName.FixSystem.DELETE_PERMISSION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      this.confirmModal.isLoading = false;
      return;
    }
    let params = new HttpParams().set("permissionId", this.permissionDeleteId!);
    this.common.base.delete(endpoint, params)
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
    this.confirmModal.showConfirmation = true;
  }

  openAddModal(){
    this.permissionAdd.isShow = true;
  }

  addNewPermission(permissionDto: AddPermissionDto){
    const endpoint = this.common.getPermission(PermissionName.FixSystem.ADD_NEW_PERMISSION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      this.permissionAdd.isLoading = false;
      return;
    }
    this.common.base.post(endpoint, permissionDto)
      .pipe(finalize(() => {
        this.permissionAdd.isLoading = false;
      }))
      .subscribe({
        next: (res) => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000)
          this.loadData();
          this.permissionAdd.onClose();
        },
        error: (err) => {
          this.common.errorHandle.handle(err);
        }
      });
  }
}
