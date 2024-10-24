import { Component, OnInit } from '@angular/core';
import { Function } from '../../../model/function';
import { FunctionService } from '../../../service/function/function.service';
import { RoleService } from '../../../service/role/role.service';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { Role } from '../../../model/role';
import { ApiStatus } from '../../../constant/api.const.urls';
import { FunctionResponse } from '../../../model/dto/function-response';
import { Permission } from '../../../model/permission';

@Component({
  selector: 'app-function',
  templateUrl: './function.component.html',
  styleUrl: './function.component.scss'
})
export class FunctionComponent implements OnInit {

  showModal = false;
  showConfirmation = false;
  roles: Role[] = [];

  currentFunction!: Function

  listFunction: FunctionResponse[] = []


  constructor(private functionService: FunctionService,
    private errorHandleService: ErrorHandleService,
    private snackbarService: SnackBarService
  ) {
  }
  ngOnInit(): void {
    this.loadList();
    this.isLoading = false;
  }

  openAddModal() {
    this.currentFunction = {} as Function
    this.showModal = true;
  }

  openUpdateModal(func: Function){
    this.currentFunction = {...func}
    this.showModal = true;
  }


  addFunction() {
    this.loadList();
    this.showModal = false;
  }

  updateFunction(func: Function){
    const functionIndex  = this.listFunction.findIndex(f => f.id == func.id)
    if(functionIndex != -1){
      this.listFunction[functionIndex].name = func.name;
    }
    this.showModal = false;
  }

  loadList() {
    this.functionService.getFunctions().subscribe({
      next: async (response) => {
        this.listFunction = response;
        if (this.openSectionId) {
          const func: Function | undefined = this.listFunction.find(func => func.id === this.openSectionId);
        }
      },
      error: (err) => {
        this.errorHandleService.handle(err);
      }
    })
  }

  addPermission(func: FunctionResponse){
    this.functionService.getAllPermissionDetail(func.id).subscribe({
      next: (res) => {
        func.permissions = res;
      },
      error: (err) => {
        this.errorHandleService.handle(err);
      }
    })
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

  deleteFunction() {
    this.functionService.deleteFunction(this.currentFunction.id!).subscribe({
      next: (res) => {
        this.snackbarService.show(null, res.content, ApiStatus.SUCCESS, 5000)
        this.listFunction = this.listFunction.filter(f=>f.id!=this.currentFunction.id);
        this.currentFunction = {} as Function
        this.cancleConfirm();
      },
      error: (err) => {
        this.errorHandleService.handle(err);
      }
    });
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
    if (true) {

    }

    const currentFunc: Function = {
      id: func.id,
      name: func.name,
    };

    this.functionService.updateFunction(currentFunc).subscribe({
      next: (response) => {
        this.snackbarService.show(null, response.content, ApiStatus.SUCCESS, 5000);
      },
      error: (err) => {
        this.errorHandleService.handle(err);
      }
    })
  }

  onChangeFunc(functionDto: Function, index: number) {
    if (true) {

    }

    const currentFunc: Function = {
      id: functionDto.id,
      name: functionDto.name,
    };

    this.functionService.updateFunction(currentFunc).subscribe({
      next: (response) => {
        this.snackbarService.show(null, response.content, ApiStatus.SUCCESS, 5000);
      },
      error: (err) => {
        this.errorHandleService.handle(err);
      }
    })
  }
}

