import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { ApprovalStatus, MyRequest, OpenSupplierReq } from '../../../model/approval';
import { ConfirmModalComponent } from '../../common/confirm-modal/confirm-modal.component';
import { CommonService } from '../../../service/common/common.service';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-my-supplier-request-detail',
  templateUrl: './my-supplier-request-detail.component.html',
  styleUrl: './my-supplier-request-detail.component.scss'
})
export class MySupplierRequestDetailComponent {
  @Input() request!: MyRequest;
  @Input() common!: CommonService
  ApprovalStatus = ApprovalStatus
  isShow = false;
  openSupplierReq: OpenSupplierReq | null = null; // Đối tượng chứa thông tin Supplier từ request.data
  @ViewChild('confirmModal') confirmModal!: ConfirmModalComponent;
  @Output() successEvent = new EventEmitter();


  public openDetail(res: MyRequest) {
    this.isShow = true;
    this.request = res
    this.openSupplierReq = JSON.parse(this.request.data);
  }

  public closeModal(){
    this.isShow = false;
    this.request = {} as MyRequest;
    this.openSupplierReq = {} as OpenSupplierReq;
  }

  public confirmCancelRequest(){
    this.confirmModal.showConfirmation = true;
  }

  public cancelRequest(){
    const endpoint = this.common.getPermission(PermissionName.SupplierInfomation.CANCEL_REQUEST)
      if (!endpoint) {
        this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        return;
      }
      this.confirmModal.isLoading = true;
      const cancelRequest = {
        id: this.request.id
      }
      this.common.base.put(endpoint, cancelRequest)
        .pipe(finalize(() => {
          this.confirmModal.isLoading = false;
        }))
        .subscribe({
          next: (response) => {
            this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000)
            this.successEvent.emit();
            this.closeModal();
          },
          error: (error) => {
            this.common.errorHandle.handle(error)
          }
        });
  }
}
