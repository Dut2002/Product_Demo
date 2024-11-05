import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ApiStatus } from '../../../constant/api.const.urls';
import { Permission } from '../../../model/permission';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
import { CommonService } from '../../../service/common/common.service';

@Component({
  selector: 'app-permission-details',
  templateUrl: './permission-details.component.html',
  styleUrl: './permission-details.component.scss'
})
export class PermissionDetailsComponent implements OnInit {
  @Input() permission!: Permission
  @Input() common!: CommonService
  @Output() updateEvent = new EventEmitter<Permission>()
  @Output() deleteEvent = new EventEmitter()
  savePermission!: Permission
  showConfirmation = false

  constructor(private errorHandleService: ErrorHandleService,
    private snackBarService: SnackBarService
  ) { }

  ngOnInit(): void {
    this.savePermission = { ...this.permission }

  }

  loadPermissionDetail() {
    // this.functionService.getPermissionDetail(this.permission.id).subscribe({
    //   next: (response) => {
    //     this.permission = response;
    //   },
    //   error: (err) => {
    //     this.errorHandleService.handle(err)
    //   }
    // })
  }

  updatePermission() {
    // this.functionService.updatePermission(this.permission).subscribe({
    //   next: (res) => {
    //     this.snackBarService.show(null, res.content, ApiStatus.SUCCESS, 5000);
    //     this.updateEvent.emit(this.permission);
    //     this.loadPermissionDetail()
    //   },
    //   error: (err) => {
    //     this.errorHandleService.handle(err);
    //   }
    // })
  }

  resetForm() {
    this.permission = { ...this.savePermission };
  }

  deleteConfirm() {
    this.showConfirmation = true;
  }

  deletePermission() {
    // this.functionService.deletePermission(this.permission.id).subscribe({
    //   next: (res) => {
    //     this.snackBarService.show(null, res.content, ApiStatus.SUCCESS, 5000);
    //     this.deleteEvent.emit();
    //     this.closeModal();
    //   },
    //   error: (err) => {
    //     this.errorHandleService.handle(err);
    //   }
    // })
  }

  closeModal() {
    this.showConfirmation = false;
  }

}


