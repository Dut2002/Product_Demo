import { HttpParams } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { finalize } from 'rxjs';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { Pattern } from '../../../constant/paterns.const';
import { Permission } from '../../../model/permission';
import { CommonService } from '../../../service/common/common.service';
import { ConfirmModalComponent } from '../../common/confirm-modal/confirm-modal.component';

@Component({
  selector: 'app-permission-details',
  templateUrl: './permission-details.component.html',
  styleUrl: './permission-details.component.scss'
})
export class PermissionDetailsComponent implements OnInit {
  @Input() permission!: Permission;
  @Input() common!: CommonService;
  @Output() updateEvent = new EventEmitter<Permission>();
  @Output() deleteEvent = new EventEmitter<boolean>();
  savePermission!: Permission;
  pattern = Pattern.endPointPattern;
  isLoading = false
  confirmTitle = ''

  @ViewChild('confirmModal') confirmModal!: ConfirmModalComponent;

  constructor(
  ) { }

  ngOnInit(): void {
    this.savePermission = { ...this.permission }
  }

  updatePermission(form: NgForm) {
    if (form.valid) {
      this.isLoading = true;
      const endpoint = this.common.getPermission(PermissionName.UPDATE_PERMISSION);
      if (!endpoint) {
        this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        return;
      }
      this.common.base.put(endpoint, this.permission).pipe(
        finalize(() => {
          this.isLoading = false;
        })
      ).subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000);
          this.savePermission = {...this.permission}
          this.updateEvent.emit(this.permission)
        },
        error: err => this.common.errorHandle.handle(err)
      })
    } else {
      form.control.markAllAsTouched();
    }
  }

  resetForm(form: NgForm) {
    form.control.markAsUntouched;
    this.permission = { ...this.savePermission };
  }

  deleteConfirm() {
    const endpoint = this.common.getPermission(PermissionName.CHECK_DELETE_PERMISSION);
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    const params = new HttpParams().set('id', this.permission.id)
    this.common.base.get(endpoint, params)
    .subscribe({
      next: res => {
        this.confirmTitle = res.content
        this.confirmModal.showConfirmation = true;
      },
      error: err => this.common.errorHandle.handle(err)
    })
  }

  deletePermission() {
    const endpoint = this.common.getPermission(PermissionName.DELETE_PERMISSION);
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    const params = new HttpParams().set('id', this.permission.id)
    this.common.base.delete(endpoint, params).pipe(
      finalize(() => {
        this.confirmModal.isLoading = false;
      })
    ).subscribe({
      next: res => {
        this.common.snackBar.show(null, res.message, ApiStatus.SUCCESS, 5000);
        this.confirmModal.close();
        this.deleteEvent.emit(res.deleteFunc);
      },
      error: err => this.common.errorHandle.handle(err)
    })
  }

}


