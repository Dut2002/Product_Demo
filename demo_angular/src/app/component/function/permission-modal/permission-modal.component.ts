import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Pattern } from '../../../constant/paterns.const';
import { CommonService } from '../../../service/common/common.service';
import { AddPermissionDto } from '../../../model/dto/add-permission-dto';
import { Permission } from '../../../model/permission';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-permission-modal',
  templateUrl: './permission-modal.component.html',
  styleUrl: './permission-modal.component.scss'
})
export class PermissionModalComponent {
  pattern = Pattern.endPointPattern;

  @Input() common!: CommonService
  @Input() functionId!: number;

  isLoading = false;
  isShow = false;

  @Output() addEvent = new EventEmitter<AddPermissionDto>();

  permission: Permission = {} as Permission;

  addPermission(form: NgForm) {
    if (form.valid) {
      this.isLoading = true;
      let permissionDto: AddPermissionDto = {
        functionId: this.functionId,
        name: this.permission.name,
        beEndPoint: this.permission.beEndPoint,
        defaultPermission: this.permission.defaultPermission,
      }
      this.addEvent.emit(permissionDto);

    } else {
      form.control.markAllAsTouched();
    }
  }

  onReset(form: NgForm) {
    form.control.markAsUntouched();
  }

  onClose() {
    this.isShow = false;
    this.permission = {} as Permission;
  }
}
