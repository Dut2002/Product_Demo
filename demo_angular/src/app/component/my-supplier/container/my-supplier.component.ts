import { Component } from '@angular/core';
import { CommonService } from '../../../service/common/common.service';
import { ActivatedRoute } from '@angular/router';
import { PermissionName } from '../../../constant/api.const.urls';
import { SupplierInfor } from '../../../model/supplier/supplier-info';

@Component({
  selector: 'app-my-supplier',
  templateUrl: './my-supplier.component.html',
  styleUrl: './my-supplier.component.scss'
})
export class MySupplierComponent {

  supplier! : SupplierInfor
  hasSupplier: boolean|null = null;

  constructor(public common: CommonService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.common.setFunctionName(this.route)
    this.loadMySupplier();
  }

  loadMySupplier() {
    const endpoint = this.common.getPermission(PermissionName.SupplierInfomation.VIEW_SUPPLIER_INFO)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.get(endpoint).subscribe({
      next: (response) => {
        if(response){
          this.supplier = response;
          this.hasSupplier = true;
        }else{
          this.hasSupplier = false;
        }
      },
      error: (error) => {
        this.common.errorHandle.handle(error)
      }
    });
  }
}
