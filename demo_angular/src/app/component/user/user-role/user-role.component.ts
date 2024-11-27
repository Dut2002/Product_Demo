import { Component, Input, OnInit } from '@angular/core';
import { CommonService } from '../../../service/common/common.service';
import { RoleDto } from '../../../model/account-management/role-dto';
import { SearchBoxDto } from '../../../model/dto/search-box-dto';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-user-role',
  templateUrl: './user-role.component.html',
  styleUrl: './user-role.component.scss'
})
export class UserRoleComponent implements OnInit {

  @Input() common!: CommonService;
  @Input() roles: RoleDto[] = [];
  @Input() accountId!: number;

  roleSearchBox: SearchBoxDto[] = []
  isShow = false;
  currentRoleId: number[] = [];
  ngOnInit(): void {
    this.loadRole();
  }

  loadRole() {
    const endpoint = this.common.getPermission(PermissionName.UserManagement.GET_ROLE_SEARCH)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }

    let params = new HttpParams().set("id", this.accountId)

    this.common.base.get(endpoint, params).subscribe({
      next: (response) => {
        console.log(response);

        this.roleSearchBox = response;
      },
      error: (err) => {
        this.common.errorHandle.handle(err);
      }
    }
    )
  }

  close() {
    this.isShow = false;
    this.currentRoleId = [];
  }

  show() {
    this.isShow = true;
    this.loadRole();
  }

  addUserRole() {
    if (this.currentRoleId.length <= 0) {
      this.common.errorHandle.show("Invalid input", "Choose at least 1 role to add");
    } else {
      const endpoint = this.common.getPermission(PermissionName.UserManagement.ADD_ROLE_USER)
      if (!endpoint) {
        this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        return;
      }
      const addRoleUser = {
        accountId: this.accountId,
        roleId: this.currentRoleId
      }

      this.common.base.post(endpoint, addRoleUser).subscribe({
        next: (response) => {
          this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000);
          // Cập nhật danh sách roles trực tiếp
          this.currentRoleId.forEach(id => {
            const role = this.roleSearchBox.find(r => r.id === id);
            if (role) {
              this.roles.push({...role}); // Thêm role mới vào danh sách
            }
          });

          this.close();
        },
        error: (err) => {
          this.common.errorHandle.handle(err);
        }
      }
      )
    }
  }

  deleteUserRole(roleId: number) {
    const endpoint = this.common.getPermission(PermissionName.UserManagement.DELETE_ROLE_USER)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    const deleteRoleUser = {
      accountId: this.accountId,
      roleId: roleId
    }

    this.common.base.deleteBody(endpoint, deleteRoleUser).subscribe({
      next: (response) => {
        this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000);
        this.roles = this.roles.filter(role => role.id !== roleId); // Loại bỏ role đã xóa
      },
      error: (err) => {
        this.common.errorHandle.handle(err);
      }
    }
    )
  }
}
