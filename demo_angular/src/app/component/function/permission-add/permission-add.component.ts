import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AddPermissionDto } from '../../../model/dto/add-permission-dto';
import { Permission } from '../../../model/permission';
import { Pattern } from '../../../constant/paterns.const';
import { CommonService } from '../../../service/common/common.service';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-permission-add',
  templateUrl: './permission-add.component.html',
  styleUrl: './permission-add.component.scss'
})
export class PermissionAddComponent {

  pattern = Pattern.endPointPattern;

  @Input() common!: CommonService
  @Input() functionId!: number;

  isLoading = false;
  isShow = false;

  @Output() addEvent = new EventEmitter();

  permission: Permission = {} as Permission;

  addPermission(form: NgForm) {
    if (form.valid) {
      this.isLoading = true;

      const endpoint = this.common.getPermission(PermissionName.ADD_PERMISSION)
      if (!endpoint) {
        this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        this.isLoading = false;
        return;
      }
      let permissionDto: AddPermissionDto = {
        functionId: this.functionId,
        name: this.permission.name,
        beEndPoint: this.permission.beEndPoint,
        defaultPermission: this.permission.defaultPermission,
      }
      this.common.base.post(endpoint, permissionDto)
        .pipe(finalize(() => {
          this.isLoading = false;
        }))
        .subscribe({
          next: (res) => {
            this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000)
            this.addEvent.emit()
            this.isShow = false;
          },
          error: (err) => {
            this.common.errorHandle.handle(err);
          }
        });
    } else {
      form.control.markAllAsTouched();
    }
  }

  onReset(form: NgForm) {
    form.control.markAsUntouched();
  }

  onShow() {
    this.isShow = true;
  }

  onClose() {
    this.isShow = false;
    this.permission = {} as Permission;
  }
}
