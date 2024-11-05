import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { RouterUrl } from '../../../constant/app.const.router';
import { RoleRes } from '../../../model/dto/role-res';
import { Role } from '../../../model/role';
import { CommonService } from '../../../service/common/common.service';

@Component({
  selector: 'app-role-permission',
  templateUrl: './role-permission.component.html',
  styleUrl: './role-permission.component.scss'
})
export class RolePermissionComponent implements OnInit {
  showModal = false;
  modalLoading = false
  showConfirmation = false;
  confirmLoading = false;
  currentRole!: Role
  title!: string
  roleList!: RoleRes[]

  constructor(
    private common: CommonService,
    private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.common.setFunctionName(this.route);
    this.loadData();

  }

  loadData() {
    const endpoint = this.common.getPermission(PermissionName.VIEW_ROLES);
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.get(endpoint).subscribe({
      next: res => {
        this.roleList = res;
      },
      error: err => {
        this.common.errorHandle.handle(err);
      }
    });
  }

  confirmDelete(role: RoleRes) {
    this.showConfirmation = true
    this.currentRole = { ...role }
  }

  closeConfirm() {
    this.showConfirmation = false;
  }

  deleteRole() {
    const endpoint = this.common.getPermission(PermissionName.DELETE_ROLE);
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams();
    params = params.set("id", this.currentRole.id)
    this.common.base.delete(endpoint, params).pipe(
      finalize(() => {
        this.confirmLoading = false;
      }))
      .subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000);
          this.roleList = this.roleList.filter(r => r.id !== this.currentRole.id);
        },
        error: err => this.common.errorHandle.handle(err)
      })
    this.closeConfirm();
  }

  viewPermission(roleId: number) {
    this.common.router.navigate([RouterUrl.USER_PERMISION],{
      queryParams: {
        roleId: roleId
      }
    });
  }

  openAddModal() {
    this.currentRole = {} as Role
    this.title = "Add New Role"
    this.showModal = true;
  }

  openUpdateModal(role: Role) {
    this.currentRole = { ...role }
    this.showModal = true;
    this.title = "Update Role"
  }

  closeModal() {
    this.showModal = false;
  }

  AddRole(role: Role) {
    const endpoint = this.common.getPermission(PermissionName.ADD_ROLE);
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.post(endpoint, role).pipe(
      finalize(() => {
        this.modalLoading = false;
      }))
      .subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000)
          this.loadData();
          this.closeModal();
        },
        error: err => this.common.errorHandle.handle(err),
      });
  }

  UpdateRole(role: Role) {
    const endpoint = this.common.getPermission(PermissionName.CHANGE_ROLE_NAME);
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.put(endpoint, role).pipe(
      finalize(() => {
        this.modalLoading = false;
      }))
      .subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000);
          const roleIndex = this.roleList.findIndex(r => r.id == role.id);
          if (roleIndex !== -1) {
            this.roleList[roleIndex].name = role.name;
          }
          this.closeModal();
        },
        error: err => this.common.errorHandle.handle(err),
      })
  }
}
