import { Component, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PermissionName } from '../../../constant/api.const.urls';
import { SupplierInfor } from '../../../model/supplier/supplier-info';
import { CommonService } from '../../../service/common/common.service';
import { ConfirmModalComponent } from '../../common/confirm-modal/confirm-modal.component';
import { SupplierFilter } from '../../../model/filter/search-filter';

@Component({
  selector: 'app-supplier-manager',
  templateUrl: './supplier-manager.component.html',
  styleUrl: './supplier-manager.component.scss'
})
export class SupplierManagerComponent {
  // @ViewChild('supplierModal') productModal!: ProductModalComponent;
  @ViewChild('confirmModal') confirmModal!: ConfirmModalComponent;

  suppliers: SupplierInfor[] = [];
  currentSupplier!: SupplierInfor

  supplierFilter: SupplierFilter = new SupplierFilter;

  totalPages: number = 0;

  constructor(public common: CommonService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.common.setFunctionName(this.route)
    this.loadSuppliers();
  }


  loadSuppliers() {
    const endpoint = this.common.getPermission(PermissionName.SupplierManagement.VIEW_SUPPLIER)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.post(endpoint, this.supplierFilter).subscribe({
      next: (response) => {
        this.suppliers = response.content; // Gán dữ liệu vào products
        this.totalPages = response.totalPages;
      },
      error: (error) => {
        this.common.errorHandle.handle(error)
      }
    });
  }

  openAddModal() {

  }

  openUpdateModal(supplier: any) {

  }

  openDeleteConfirm(supplier: any) {

  }

  onPageChange(page: number) {
    this.supplierFilter.pageNum = page;
    this.loadSuppliers();
  }

  deleteSupplier() {

  }
}
