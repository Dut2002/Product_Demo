import { HttpParams } from '@angular/common/http';
import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { PermissionName } from '../../../constant/api.const.urls';
import { FunctionDto } from '../../../model/dto/function-dto';
import { Permission } from '../../../model/permission';
import { CommonService } from '../../../service/common/common.service';
import { PermissionAddComponent } from '../permission-add/permission-add.component';

@Component({
  selector: 'app-function-detail',
  templateUrl: './function-detail.component.html',
  styleUrl: './function-detail.component.scss'
})
export class FunctionDetailComponent {

  @Input() common!: CommonService;
  @Input() func!: FunctionDto
  @Output() deleteEvent = new EventEmitter<number>();

  @ViewChild('actionTab', { static: false }) actionTab!: PermissionAddComponent

  loadData() {
    const endpoint = this.common.getPermission(PermissionName.FunctionManagement.GET_PERMISSIONS)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    const params = new HttpParams().set('functionId', this.func.id)
    this.common.base.get(endpoint, params)
      .subscribe({
        next: (res) => {
          this.func.permissions = res;
        },
        error: (err) => {
          this.common.errorHandle.handle(err);
        }
      });
  }

  updatePermission(permissionId: number, newPermission: Permission) {
    // Tìm đối tượng cần cập nhật trong danh sách permissions
    const permissionIndex = this.func.permissions.findIndex(p => p.id === permissionId);

    if (permissionIndex !== -1) {
      // Nếu tìm thấy, cập nhật đối tượng bằng newPermission
      this.func.permissions[permissionIndex] = newPermission;
    }
  }

  deletePermission(permissionId: number, deleteFunc: boolean) {
    if (deleteFunc) {
      this.deleteEvent.emit(this.func.id);
    } else {
      this.func.permissions = this.func.permissions.filter(p => p.id != permissionId);
    }
  }
}
