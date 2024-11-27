import { HttpParams } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { NgModel } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs';
import { ApiStatus, PermissionName } from '../../constant/api.const.urls';
import { FunctionDto } from '../../model/dto/function-dto';
import { SearchBoxDto } from '../../model/dto/search-box-dto';
import { Function } from '../../model/function';
import { CommonService } from '../../service/common/common.service';
import { ConfirmModalComponent } from '../common/confirm-modal/confirm-modal.component';
import { FunctionModalComponent } from '../function/function-modal/function-modal.component';

@Component({
  selector: 'app-fix-system',
  templateUrl: './fix-system.component.html',
  styleUrl: './fix-system.component.scss'
})
export class FixSystemComponent {
  @ViewChild('confirmModal') confirmModal!: ConfirmModalComponent;
  @ViewChild('actionModal') actionModal!: FunctionModalComponent;


  functionSearch: SearchBoxDto[] = []
  funcAddId!: number | null
  funcDeleteId!: number | null
  functions: FunctionDto[] = [];
  constructor(public common: CommonService,
    private route: ActivatedRoute
  ) {
    this.common.setFunctionName(this.route);
    this.loadData();
  }

  DeleteFunction() {
    this.confirmModal.isLoading = true;
    const endpoint = this.common.getPermission(PermissionName.FixSystem.DELETE_FUNCTION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      this.confirmModal.isLoading = false;
      return;
    }
    let params = new HttpParams().set("functionId", this.funcDeleteId!);
    this.common.base.delete(endpoint, params)
      .pipe(finalize(() => {
        this.confirmModal.isLoading = false;
      }))
      .subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000);
          this.confirmModal.close();
          this.functions = this.functions.filter(f => f.id !== this.funcDeleteId);
          this.funcDeleteId = null;
        },
        error: err => this.common.errorHandle.handle(err)
      })
  }

  ConfirmDelete(funcId: number) {
    this.funcDeleteId = funcId;
    this.confirmModal.showConfirmation = true;
  }



  loadData() {
    const endpoint = this.common.getPermission(PermissionName.FixSystem.VIEW_SUPER_ADMIN_FUNCTION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.get(endpoint).subscribe({
      next: res => {
        this.functions = res;
      },
      error: err => this.common.errorHandle.handle(err)
    })
  }

  loadFunctionSearch() {
    const endpoint = this.common.getPermission(PermissionName.FixSystem.GET_FUNCTION_SEARCH)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.get(endpoint).subscribe({
      next: res => this.functionSearch = res,
      error: err => this.common.errorHandle.handle(err)
    });
  }

  openAddModal() {
    this.actionModal.showModal = true;
  }

  addNewFunction(func: Function) {
    const endpoint = this.common.getPermission(PermissionName.FixSystem.ADD_NEW_FUNCTION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      this.actionModal.isLoading = false;
      return;
    }
    this.common.base.post(endpoint, func)
      .pipe(
        finalize(() => {
          this.actionModal.isLoading = false;
        }))
      .subscribe({
        next: (response) => {
          this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000);
          this.loadData();
          this.actionModal.close();
        }
        ,
        error: (error) => {
          this.common.errorHandle.handle(error);
        }
      })
  }

  addFunction(func: NgModel) {
    if (func.valid) {
      const endpoint = this.common.getPermission(PermissionName.FixSystem.ADD_FUNCTION)
      if (!endpoint) {
        this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        return;
      }
      const body = {
        functionId: this.funcAddId,
      }
      this.common.base.post(endpoint, body).subscribe({
        next: res => {
          this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000);
          this.loadData();
          this.funcAddId = null
        },
        error: err => this.common.errorHandle.handle(err)
      })
    } else {
      this.common.errorHandle.show("Invalid", "Choose a Function to Add")
    }
  }

}
