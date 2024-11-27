import { animate, style, transition, trigger } from '@angular/animations';
import { HttpParams } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { RouterUrl } from '../../../constant/app.const.router';
import { RoleRes } from '../../../model/dto/role-res';
import { Role } from '../../../model/role';
import { CommonService } from '../../../service/common/common.service';
import { ConfirmModalComponent } from '../../common/confirm-modal/confirm-modal.component';
import { RoleModalComponent } from '../role-modal/role-modal.component';

@Component({
  selector: 'app-role-permission',
  templateUrl: './role-permission.component.html',
  styleUrl: './role-permission.component.scss',
  animations: [
    // Animation cho danh sách khi một phần tử bị xóa
    trigger('listAnimation', [
      transition(':leave', [
        style({ opacity: 1, transform: 'translateY(0)' }),
        animate('0.3s ease-out', style({ opacity: 0, transform: 'translateY(-100%)' })),
      ]),
    ]),
    // Animation cho các phần tử còn lại khi có phần tử bị xóa
    trigger('listMoveUp', [
      transition(':enter', [
        style({ transform: 'translateY(30px)', opacity: 0 }), // Khởi tạo phần tử ở dưới
        animate('0.3s ease-out', style({ transform: 'translateY(0)', opacity: 1 })) // Di chuyển lên và xuất hiện
      ]),
      transition(':leave', [
        style({ transform: 'translateY(0)', opacity: 1 }),
        animate('0.3s ease-out', style({ transform: 'translateY(-10px)', opacity: 0 })) // Di chuyển lên và mờ dần
      ])
    ])
  ],
})
export class RolePermissionComponent implements OnInit {
  currentRole!: Role
  title!: string
  roleList!: RoleRes[]

  @ViewChild('confirmModal') confirmModal!: ConfirmModalComponent;
  @ViewChild('actionModal') actionModal!: RoleModalComponent;

  constructor(
    public common: CommonService,
    private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.common.setFunctionName(this.route);
    this.loadData();

  }

  loadData() {
    const endpoint = this.common.getPermission(PermissionName.UserRole.VIEW_ROLES);
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
    this.confirmModal.showConfirmation = true;
    this.currentRole = { ...role }
  }

  closeConfirm() {
    this.confirmModal.showConfirmation = false;
  }

  deleteRole() {
    const endpoint = this.common.getPermission(PermissionName.UserRole.DELETE_ROLE);
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams();
    params = params.set("id", this.currentRole.id)
    this.common.base.delete(endpoint, params).pipe(
      finalize(() => {
        this.confirmModal.isLoading = false;
      }))
      .subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000);
          // this.roleList = this.roleList.filter(r => r.id !== this.currentRole.id);
          const index = this.roleList.findIndex(r => r.id === this.currentRole.id);
          if (index !== -1) {
            this.roleList.splice(index, 1);
          }

        },
        error: err => this.common.errorHandle.handle(err)
      })
    this.confirmModal.close();
  }

  viewPermission(roleId: number) {
    this.common.router.navigate([RouterUrl.USER_PERMISION], {
      queryParams: {
        roleId: roleId
      }
    });
  }

  openAddModal() {
    this.currentRole = {} as Role
    this.title = "Add New Role"
    this.actionModal.showModal = true;
  }

  openUpdateModal(role: Role) {
    this.currentRole = { ...role }
    this.actionModal.showModal = true;
    this.title = "Update Role"
  }

  AddRole(role: Role) {
    const endpoint = this.common.getPermission(PermissionName.UserRole.ADD_ROLE);
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.post(endpoint, role).pipe(
      finalize(() => {
        this.actionModal.isLoading = false;
      }))
      .subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000)
          this.loadData();
          this.actionModal.closeModal();
        },
        error: err => this.common.errorHandle.handle(err),
      });
  }

  UpdateRole(role: Role) {
    const endpoint = this.common.getPermission(PermissionName.UserRole.CHANGE_ROLE_NAME);
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.put(endpoint, role).pipe(
      finalize(() => {
        this.actionModal.isLoading = false;
      }))
      .subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000);
          const index = this.roleList.findIndex(role => role.id === this.currentRole.id);
          if (index !== -1) {
            // Cập nhật lại danh sách sau khi xóa
            this.roleList = [...this.roleList.slice(0, index), ...this.roleList.slice(index + 1)];
          }
          this.actionModal.closeModal();
        },
        error: err => this.common.errorHandle.handle(err),
      })
  }

  trackByRoleId(index: number, role: any) {
    return role.id;
  }
}
