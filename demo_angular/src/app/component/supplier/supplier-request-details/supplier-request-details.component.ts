import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { ApprovalStatus, OpenSupplierReq, RequestDto } from '../../../model/approval';
import { CommonService } from '../../../service/common/common.service';
import { ConfirmModalComponent } from '../../common/confirm-modal/confirm-modal.component';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-supplier-request-details',
  templateUrl: './supplier-request-details.component.html',
  styleUrl: './supplier-request-details.component.scss'
})
export class SupplierRequestDetailsComponent {
  @Input() request!: RequestDto;
  @Input() common!: CommonService
  isApprove!: boolean
  ApprovalStatus = ApprovalStatus
  isShow = false;
  @ViewChild('confirmModal') confirmModal!: ConfirmModalComponent;
  @Output() successEvent = new EventEmitter();



  public openDetail(res: RequestDto) {
    this.isShow = true;
    this.request = res
  }

  public closeModal() {
    this.isShow = false;
    this.request = {} as RequestDto;
  }

  public confirmProcesslRequest(process: boolean) {
    this.isApprove = process;
    this.confirmModal.showConfirmation = this.isShow;
  }

  saveNote(noteInput: HTMLTextAreaElement) {
    if(!noteInput.value || noteInput.value.trim() === ''){
      this.common.errorHandle.show('Invalid input', 'Không để note trống')
      return;
    }

    const endpoint = this.common.getPermission(PermissionName.SupplierApproval.SAVE_NOTE)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.confirmModal.isLoading = true;
    const saveNoteRequest = {
      id: this.request.id,
      note: noteInput.value.trim()
    }
    this.common.base.put(endpoint, saveNoteRequest)
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

  processRequest() {
    const endpoint = this.common.getPermission(PermissionName.SupplierApproval.PROCESS_REQUEST)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.confirmModal.isLoading = true;
    const processRequest = {
      id: this.request.id,
      status: this.isApprove === true ? ApprovalStatus.APPROVED : ApprovalStatus.REJECTED,
      approvalType: this.request.approvalType
    }
    this.common.base.put(endpoint, processRequest)
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
