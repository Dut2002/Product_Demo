import { HttpParams } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { FunctionDto } from '../../../model/dto/function-dto';
import { FunctionResponse } from '../../../model/dto/function-response';
import { Function } from '../../../model/function';
import { Permission } from '../../../model/permission';
import { Role } from '../../../model/role';
import { CommonService } from '../../../service/common/common.service';
import { AddPermissionDto } from '../../../model/dto/add-permission-dto';
import { PermissionAddComponent } from '../permission-add/permission-add.component';

@Component({
  selector: 'app-function',
  templateUrl: './function.component.html',
  styleUrl: './function.component.scss'
})
export class FunctionComponent implements OnInit {

  showModal = false;
  modalLoading = false;
  modalTitle = ''
  showConfirmation = false;
  confirmLoading = false;
  showAdd = false;
  addLoading = false;




  roles: Role[] = [];
  currentFunction!: Function
  listFunction!: FunctionDto[];

  constructor(private common: CommonService,
    private route: ActivatedRoute) {

  }

  @ViewChild('permissionAdd') permissionAdd!: PermissionAddComponent;


  ngOnInit(): void {
    this.common.setFunctionName(this.route);
    this.loadList();
    this.isLoading = false;
  }



  loadList() {
    const endpoint = this.common.getPermission(PermissionName.VIEW_FUNCTIONS)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.get(endpoint).subscribe({
      next: async (response) => {
        this.listFunction = response;
        if (this.openSectionId) {
          const func: Function | undefined = this.listFunction.find(func => func.id === this.openSectionId);
        }
      },
      error: (err) => {
        this.common.errorHandle.handle(err);
      }
    })
  }

  openAddModal() {
    this.currentFunction = {} as Function
    this.showModal = true;
    this.modalTitle = 'Add new Function'
  }

  openUpdateModal(func: Function) {
    this.currentFunction = { ...func }
    this.showModal = true;
    this.modalTitle = 'Update Function'
  }


  addFunction(func: Function) {
    const endpoint = this.common.getPermission(PermissionName.ADD_FUNCTION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.addLoading = true;
    this.common.base.post(endpoint, func)
      .pipe(
        finalize(() => {
        this.addLoading = false;
      }))
      .subscribe({
        next: (response) => {
          this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000);
          this.loadList();
          this.showAdd = false;
        }
        ,
        error: (error) => {
          this.common.errorHandle.handle(error);
        }
      })
  }

  updateFunction(func: Function) {
    const endpoint = this.common.getPermission(PermissionName.UPDATE_FUNCTION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.modalLoading = true;
    this.common.base.put(endpoint, func)
      .pipe(finalize(() => {
        this.modalLoading = false; // Sẽ chạy dù thành công hay gặp lỗi
      }))
      .subscribe({
        next: (response) => {
          this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000);
          const functionIndex = this.listFunction.findIndex(f => f.id == func.id)
          if (functionIndex != -1) {
            this.listFunction[functionIndex].name = func.name;
            this.listFunction[functionIndex].feRoute = func.feRoute;
          }
          this.closeModal();
        },
        error: (error) => {
          this.common.errorHandle.handle(error);
        }
      })
  }

  deleteFunction() {
    const endpoint = this.common.getPermission(PermissionName.DELETE_FUNCTION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams();
    params = params.set("id", this.currentFunction.id.toString());
    this.common.base.delete(endpoint, params).subscribe({
      next: (res) => {
        this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000)
        this.listFunction = this.listFunction.filter(f => f.id != this.currentFunction.id);
        this.currentFunction = {} as Function
        this.cancleConfirm();
      },
      error: (err) => {
        this.common.errorHandle.handle(err);
      }
    });
  }

  addPermission(permissionDto: AddPermissionDto) {
    const endpoint = this.common.getPermission(PermissionName.ADD_PERMISSION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.modalLoading = true;
    this.common.base.post(endpoint, permissionDto)
    .pipe(finalize(() => {
      this.modalLoading = false;

    }))
    .subscribe({
      next: (res) => {
        this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000)
      },
      error: (err) => {
        this.common.errorHandle.handle(err);
      }
    });
  }

  updatePermission(func: FunctionResponse, permissionId: number, newPermission: Permission) {
    // Tìm đối tượng cần cập nhật trong danh sách permissions
    const permissionIndex = func.permissions.findIndex(p => p.id === permissionId);

    if (permissionIndex !== -1) {
      // Nếu tìm thấy, cập nhật đối tượng bằng newPermission
      func.permissions[permissionIndex] = newPermission;
    } else {
      console.log('Permission not found');
    }
  }

  deletePermission(func: FunctionResponse, permissionId: number) {
    func.permissions = func.permissions.filter(p => p.id != permissionId);
  }

  addSuccess() {
    this.loadList();
    this.closeModal();
  }

  closeModal() {
    this.showModal = false;
  }

  confirmDelete(fun: Function) {
    this.showConfirmation = true;
    this.currentFunction = { ...fun }
  }



  cancleConfirm() {
    this.showConfirmation = false;
  }

  openSectionId: number | null = null;
  isLoading = true;


  async toggle(func: Function): Promise<void> {
    if (this.openSectionId === func.id) {
      this.openSectionId = null;
    } else {
      this.openSectionId = func.id;
    }
  }


  onUpdateFunc(func: Function, index: number) {
    // if (true) {

    // }

    // const currentFunc: Function = {
    //   id: func.id,
    //   name: func.name,
    // };

    // this.functionService.updateFunction(currentFunc).subscribe({
    //   next: (response) => {
    //     this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000);
    //   },
    //   error: (err) => {
    //     this.common.errorHandle.handle(err);
    //   }
    // })
  }

  onChangeFunc(functionDto: Function, index: number) {
    // if (true) {

    // }

    // const currentFunc: Function = {
    //   id: functionDto.id,
    //   name: functionDto.name,
    // };

    // this.functionService.updateFunction(currentFunc).subscribe({
    //   next: (response) => {
    //     this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000);
    //   },
    //   error: (err) => {
    //     this.common.errorHandle.handle(err);
    //   }
    // })
  }
}

