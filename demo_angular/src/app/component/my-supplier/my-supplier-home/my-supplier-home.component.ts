import { Component, Input } from '@angular/core';
import { SupplierInfor } from '../../../model/supplier/supplier-info';
import { CommonService } from '../../../service/common/common.service';
import { Role } from '../../../constant/app.const.role';

@Component({
  selector: 'app-my-supplier-home',
  templateUrl: './my-supplier-home.component.html',
  styleUrl: './my-supplier-home.component.scss'
})
export class MySupplierHomeComponent {

  role = Role.SUPPLIER;
  @Input() supplier!: SupplierInfor;
  @Input() common!: CommonService;
}
