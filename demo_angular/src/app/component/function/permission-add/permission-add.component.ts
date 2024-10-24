import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Permission } from '../../../model/permission';
import { FunctionService } from '../../../service/function/function.service';
import { AddPermissionDto } from '../../../model/dto/add-permission-dto';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { ApiStatus } from '../../../constant/api.const.urls';

@Component({
  selector: 'app-permission-add',
  templateUrl: './permission-add.component.html',
  styleUrl: './permission-add.component.scss'
})
export class PermissionAddComponent {

  @Input() functionId!: number;
  @Output() addEvent = new EventEmitter();
  isShow = false;
  permission: Permission = {} as Permission;

  constructor(private functionService: FunctionService,
    private snackBarService: SnackBarService,
    private errorHandelService: ErrorHandleService
  ){

  }

  addPermission(form: NgForm){
    if(form.valid){
      let permissionDto: AddPermissionDto = {
        functionId: this.functionId,
        name: this.permission.name,
        beEndPoint: this.permission.beEndPoint,
        feEndPoint: this.permission.feEndPoint,
      }
      console.log(permissionDto);

      this.functionService.addPermission(permissionDto).subscribe({
        next: (res) => {
          this.snackBarService.show(null, res.content, ApiStatus.SUCCESS, 5000)
          this.addEvent.emit();
          this.onClose();
        },
        error: (err) => {
          this.errorHandelService.handle(err);
        }
      });
    }else{
      form.control.markAllAsTouched();
    }
  }

  onReset(form: NgForm){
    form.control.markAsUntouched();
  }

  onShow(){
    this.isShow = true;
  }

  onClose(){
    this.isShow = false;
    this.permission = {} as Permission;
  }
}
