import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Pattern } from '../../../constant/paterns.const';
import { AddPermissionDto } from '../../../model/dto/add-permission-dto';
import { Permission } from '../../../model/permission';
import { CommonService } from '../../../service/common/common.service';

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
