import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AccountFilter } from '../../../model/filter/search-filter';
import { SearchBoxDto } from '../../../model/dto/search-box-dto';
import { AccountStatus } from '../../../model/account-management/account-status';
import { CommonService } from '../../../service/common/common.service';
import { PermissionName } from '../../../constant/api.const.urls';

@Component({
  selector: 'app-user-filter',
  templateUrl: './user-filter.component.html',
  styleUrl: './user-filter.component.scss'
})
export class UserFilterComponent {


  @Input() common!: CommonService;
  @Input() filter = {} as AccountFilter;
  @Output() applyEvent = new EventEmitter<AccountFilter>();
  @Output() resetEvent = new EventEmitter<void>();

  roleSearchBox: SearchBoxDto[] = []
  statusSearchBox: AccountStatus[] = []

  ngOnInit(): void {
    this.loadRole()
    this.loadStatus()
  }

  constructor(
  ) { }

  loadRole(){
    const endpoint = this.common.getPermission(PermissionName.UserManagement.GET_ROLE_SEARCH)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }

    this.common.base.get(endpoint).subscribe({
      next: (response) =>{
        this.roleSearchBox = response;
      },
      error: (err) => {
        this.common.errorHandle.handle(err);
      }
    }
    )
  }

  loadStatus(){
    this.statusSearchBox = Object.values(AccountStatus);
  }

  onRoleSelected(id: number|null)
  {
    this.filter.roleId = id;
  }

  onSupplierSelected(status: AccountStatus|null)
  {
    this.filter.status = status;
  }

  onApply(){
    this.filter.pageNum = 1;
    this.applyEvent.emit(this.filter);
  }

  onReset(){
    this.resetEvent.emit();
  }
}
