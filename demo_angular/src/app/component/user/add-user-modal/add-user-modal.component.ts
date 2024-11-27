import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { CommonService } from '../../../service/common/common.service';
import { AddUserReq } from '../../../model/account-management/add-user-req';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { SearchBoxDto } from '../../../model/dto/search-box-dto';
import { NgForm, NgModel } from '@angular/forms';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-add-user-modal',
  templateUrl: './add-user-modal.component.html',
  styleUrl: './add-user-modal.component.scss'
})
export class AddUserModalComponent {
  @Input() common!: CommonService;
  @Output() successAddEvent = new EventEmitter();

  addUser: AddUserReq = {} as AddUserReq;

  showModal = false;
  isLoading = false;
  roleSearchBox: SearchBoxDto[] = []

  @ViewChild('userForm', { static: false }) userForm!: NgForm
  @ViewChild('rolesSelect', { static: false }) rolesSelect!: NgModel

  loadRole() {
    const endpoint = this.common.getPermission(PermissionName.UserManagement.GET_ROLE_SEARCH)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.get(endpoint).subscribe({
      next: (response) => {
        console.log(response);

        this.roleSearchBox = response;
      },
      error: (err) => {
        this.common.errorHandle.handle(err);
      }
    });
  }

  onShow() {
    this.addUser = {} as AddUserReq;
    this.loadRole();
    this.showModal = true;
  }

  onReset() {
      this.addUser = { } as AddUserReq
      this.userForm.control.markAsUntouched();
      this.rolesSelect.control.markAsUntouched();
  }

  saveUser() {
    if(this.checkFormValid()){
      const endpoint = this.common.getPermission(PermissionName.UserManagement.ADD_USER)
      if (!endpoint) {
        this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        return;
      }
      this.isLoading = true;

      this.common.base.post(endpoint, this.addUser)
      .pipe(finalize(()=>{
        this.isLoading = false;
      }))
      .subscribe({
        next: (response) => {
          this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000);
          this.onClose();
        },
        error: (err) => {
          this.common.errorHandle.handle(err);
        }
      });
    }else{
      this.userForm.control.markAllAsTouched();
      this.rolesSelect.control.markAllAsTouched();
    }
  }

  onClose() {
    this.showModal = false;
    this.onReset();
  }

  checkFormValid() {
    return this.userForm.valid && this.rolesSelect.valid;
  }
}
