import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { CommonService } from '../../../service/common/common.service';
import { ApprovalStatus, MyRequest, OpenSupplierReq } from '../../../model/approval';
import { Pattern } from '../../../constant/paterns.const';
import { NgForm } from '@angular/forms';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { finalize } from 'rxjs';
import { MySupplierRequestDetailComponent } from '../my-supplier-request-detail/my-supplier-request-detail.component';

@Component({
  selector: 'app-my-supplier-request',
  templateUrl: './my-supplier-request.component.html',
  styleUrl: './my-supplier-request.component.scss'
})
export class MySupplierRequestComponent implements OnInit {
  @Input() common!: CommonService;
  ApprovalStatus = ApprovalStatus
  requests: MyRequest[] = [];
  currentRequest: OpenSupplierReq = {} as OpenSupplierReq;
  openRequest!: MyRequest
  pattern = Pattern
  isLoading = false;

  @ViewChild('requestDetail') requestDetail!: MySupplierRequestDetailComponent;

  ngOnInit(): void {
      this.loadData();
  }

  onReset(formRequest: NgForm) {
    formRequest.control.markAsUntouched();
  }

  disableRequest(): boolean {
    return  !this.requests || this.requests.some(r => r.status === ApprovalStatus.PENDING);
  }

  getRequest(request: MyRequest){
    this.currentRequest = JSON.parse(request.data);
  }

  sendRequest(requestForm: NgForm) {
    if (requestForm.valid) {
      const endpoint = this.common.getPermission(PermissionName.SupplierInfomation.SUPPLIER_REGISTER)
      if (!endpoint) {
        this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        return;
      }
      this.isLoading = true;
      this.common.base.post(endpoint, this.currentRequest)
        .pipe(finalize(() => {
          this.isLoading = false;
        }))
        .subscribe({
          next: (response) => {
            this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000)
            this.currentRequest = {} as OpenSupplierReq;
            this.loadData();
          },
          error: (error) => {
            this.common.errorHandle.handle(error)
          }
        });
    } else {
      requestForm.untouched
    }
  }

  loadData() {
    const endpoint = this.common.getPermission(PermissionName.SupplierInfomation.MY_SUPPLIER_REQUEST)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.get(endpoint)
      .subscribe({
        next: (response) => {
          this.requests = response;
        },
        error: (error) => {
          this.common.errorHandle.handle(error)
        }
      });

  }

  onPhoneKeyPress(event: KeyboardEvent): void {
    const allowedChars = /^[0-9+]$/;
    const key = event.key;
    if (!allowedChars.test(key)) {
      event.preventDefault();
    }
  }

  openDetail(request: MyRequest){
    this.requestDetail.openDetail(request);
  }
}



