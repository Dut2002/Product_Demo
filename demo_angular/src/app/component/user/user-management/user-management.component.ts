import { Component, ViewChild } from '@angular/core';
import { AccountDto } from '../../../model/account-management/account-dto';
import { AccountFilter } from '../../../model/filter/search-filter';
import { CommonService } from '../../../service/common/common.service';
import { ActivatedRoute } from '@angular/router';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { AddUserModalComponent } from '../add-user-modal/add-user-modal.component';
import { HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.scss'
})
export class UserManagementComponent {

  accounts: AccountDto[] = [];
  accountFilter = new AccountFilter;
  totalPages = 0;

  @ViewChild('addUserModal') addUserModal!: AddUserModalComponent;


  constructor(public common: CommonService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.common.setFunctionName(this.route)
    this.loadAccounts();
  }

  loadAccounts() {
    const endpoint = this.common.getPermission(PermissionName.UserManagement.VIEW_USERS)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.post(endpoint, this.accountFilter).subscribe({
      next: (response) => {
        this.accounts = response.content; // Gán dữ liệu vào products
        this.totalPages = response.totalPages;
      },
      error: (error) => {
        this.common.errorHandle.handle(error)
      }
    });
  }

  OpenAddModal(){
    this.addUserModal.onShow();
  }

  onPageChange(pageNum: number){
    if(pageNum > 0 && pageNum < this.totalPages) this.accountFilter.pageNum = pageNum;
  }

  applyFilter(accountFilter: AccountFilter): void {
    this.accountFilter = accountFilter;
    this.loadAccounts();
  }

  resetFilter(): void {
    this.accountFilter = new AccountFilter;
    this.loadAccounts();
  }

  changePassword(id: number){
    const endpoint = this.common.getPermission(PermissionName.UserManagement.CHANGE_PASSWORD)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    const body = {
      id: id
    }
    this.common.base.put(endpoint, body).subscribe({
      next: (response) => {
        this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000)
        this.loadAccounts();
      },
      error: (error) => {
        this.common.errorHandle.handle(error)
      }
    });
  }

  deleteAccount(id: number){
    const endpoint = this.common.getPermission(PermissionName.UserManagement.DELETE_USER)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    const params = new HttpParams().set("id", id)
    this.common.base.delete(endpoint, params).subscribe({
      next: (response) => {
        this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000)
        this.loadAccounts();
      },
      error: (error) => {
        this.common.errorHandle.handle(error)
      }
    });
  }
}
