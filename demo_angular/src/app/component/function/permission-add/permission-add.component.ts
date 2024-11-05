import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AddPermissionDto } from '../../../model/dto/add-permission-dto';
import { Permission } from '../../../model/permission';

@Component({
  selector: 'app-permission-add',
  templateUrl: './permission-add.component.html',
  styleUrl: './permission-add.component.scss'
})
export class PermissionAddComponent {

  @Input() functionId!: number;
  @Input() isLoading = false;
  @Input() isShow = false;
  @Output() addEvent = new EventEmitter<AddPermissionDto>();

  permission: Permission = {} as Permission;

  addPermission(form: NgForm) {
    if (form.valid) {
      let permissionDto: AddPermissionDto = {
        functionId: this.functionId,
        name: this.permission.name,
        beEndPoint: this.permission.beEndPoint,
        defaultPermission: this.permission.defaultPermission,
      }
      this.addEvent.emit(permissionDto)
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
