import { Component, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PermissionName } from '../../../constant/api.const.urls';
import { ApprovalStatus, RequestDto, RequestFilter } from '../../../model/approval';
import { CommonService } from '../../../service/common/common.service';
import { SupplierRequestDetailsComponent } from '../supplier-request-details/supplier-request-details.component';

@Component({
  selector: 'app-supplier-request-list',
  templateUrl: './supplier-request-list.component.html',
  styleUrl: './supplier-request-list.component.scss'
})
export class SupplierRequestListComponent {

  requests: RequestDto[] = []

  currentRequest!: RequestDto

  approvalStatus = ApprovalStatus

  requestFilter: RequestFilter = {
    pageNum: 1,
    pageSize: 8
  } as RequestFilter

  @ViewChild('requestDetail') requestDetail!: SupplierRequestDetailsComponent;

  totalPages: number = 0;

  constructor(public common: CommonService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.common.setFunctionName(this.route)
    this.loadRequests();
  }

  loadRequests() {
    const endpoint = this.common.getPermission(PermissionName.SupplierApproval.VIEW_REQUEST)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.post(endpoint, this.requestFilter).subscribe({
      next: (response) => {
        // Giả sử response.content là một mảng các đối tượng RequestDto
        this.requests = response.content.map((request: RequestDto) => {
          return {
            ...request,
            data: JSON.parse(request.data)
          };
        });
        this.totalPages = response.totalPages;
      },
      error: (error) => {
        this.common.errorHandle.handle(error)
      }
    });
  }

  openDetail(request: RequestDto) {
    this.currentRequest = {...request};
    this.requestDetail.isShow = true;
  }


  onPageChange(pageNum: number) {
    this.requestFilter.pageNum = pageNum;
    this.loadRequests();
  }
}
