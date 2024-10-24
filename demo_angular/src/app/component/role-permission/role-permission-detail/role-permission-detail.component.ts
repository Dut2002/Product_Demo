import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FunctionDto } from '../../../model/dto/function-dto';
import { PermissionDto } from '../../../model/dto/permission-dto';
import { RoleService } from '../../../service/role/role.service';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { ChangeRolePermission } from '../../../model/dto/change-role-permission';
import { PermissionRequest } from '../../../model/dto/permission-request';
import { ApiStatus } from '../../../constant/api.const.urls';

@Component({
  selector: 'app-role-permission-detail',
  templateUrl: './role-permission-detail.component.html',
  styleUrl: './role-permission-detail.component.scss'
})
export class RolePermissionDetailComponent implements OnInit {

  @Input() function!: FunctionDto
  @Input() roleId!: number
  save!: PermissionDto[]
  @Output() changeEvent = new EventEmitter<FunctionDto>()

  constructor(private roleService: RoleService,
     private snackBarService: SnackBarService,
     private errorHandleService: ErrorHandleService
  ) {

  }
  ngOnInit(): void {
    this.save = this.function.permissions.map(permission => ({ ...permission }));
  }
  updateRolePermission() {
    const role: ChangeRolePermission = {
      roleId: this.roleId,
      permissions: this.function.permissions.map(permisions => {
        const per: PermissionRequest = {
          id: permisions.permissionId,
          status: permisions.status
        }
        return per;
      })
    }
    this.roleService.changeRolePermission(role).subscribe({
      next: res => {
        this.snackBarService.show(null, res.content, ApiStatus.SUCCESS, 5000);
        this.changeEvent.emit(this.function);
        this.save = this.function.permissions.map(permission => ({ ...permission }));
      },
      error: err => this.errorHandleService.handle(err)
    })


  }

  resetForm(){
    this.function.permissions = this.save.map(permission => ({ ...permission }));
  }
}
