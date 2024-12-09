import { animate, style, transition, trigger } from '@angular/animations';
import { HttpParams } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { FunctionDto } from '../../../model/dto/function-dto';
import { Function } from '../../../model/function';
import { Role } from '../../../model/role';
import { CommonService } from '../../../service/common/common.service';
import { ConfirmModalComponent } from '../../common/confirm-modal/confirm-modal.component';
import { FunctionModalComponent } from '../function-modal/function-modal.component';

@Component({
  selector: 'app-function',
  templateUrl: './function.component.html',
  styleUrl: './function.component.scss',
  animations: [
    trigger('permissionView', [
      transition(':enter', [
        style({ height: '0px', opacity: 0, overflow: 'hidden'}), // Bắt đầu từ chiều cao 0 và mờ
        animate('{{timing}} cubic-bezier(0.4, 0.0, 0.2, 1)', style({ height: '*', opacity: 1 })) // Mở rộng chiều cao và hiển thị
      ], { params: { timing: '1s' } }),
      transition(':leave', [
        animate('{{timing}} cubic-bezier(0.4, 0.0, 0.2, 1)', style({ height: '0px', opacity: 0 })) // Thu nhỏ và ẩn
      ], { params: { timing: '0.5s' } }),
    ])
  ]
})
export class FunctionComponent implements OnInit {
[x: string]: any;

  modalTitle = ''
  openSectionId: number | null = null;

  @ViewChild('confirmModal') confirmModal!: ConfirmModalComponent;
  @ViewChild('actionModal') actionModal!: FunctionModalComponent;

  roles: Role[] = [];
  currentFunction!: Function
  listFunction!: FunctionDto[];

  constructor(public common: CommonService,
    private route: ActivatedRoute) {

  }

  getAnimationTiming(element: HTMLElement): string {
    const height = element.scrollHeight; // Lấy chiều cao thật của nội dung
    const baseDuration = 500; // Thời gian gốc (ms) cho chiều cao chuẩn
    const duration = Math.min(1000, Math.max(300, (height / 100) * baseDuration)); // Giới hạn thời gian
    return `${duration}ms`;
  }

  ngOnInit(): void {
    this.common.setFunctionName(this.route);
    this.loadList();
  }

  loadList() {
    const endpoint = this.common.getPermission(PermissionName.FunctionManagement.VIEW_FUNCTIONS)
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
    this.actionModal.showModal = true;
    this.modalTitle = 'Add new Function'
  }

  openUpdateModal(func: Function) {
    this.currentFunction = { ...func }
    this.actionModal.showModal = true;
    this.modalTitle = 'Update Function'
  }


  addFunction(func: Function) {
    const endpoint = this.common.getPermission(PermissionName.FunctionManagement.ADD_FUNCTION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
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
          this.loadList();
          this.actionModal.showModal = false;
        }
        ,
        error: (error) => {
          this.common.errorHandle.handle(error);
        }
      })
  }

  updateFunction(func: Function) {
    const endpoint = this.common.getPermission(PermissionName.FunctionManagement.UPDATE_FUNCTION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.put(endpoint, func)
      .pipe(finalize(() => {
        this.actionModal.isLoading = false;
      }))
      .subscribe({
        next: (response) => {
          this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000);
          const functionIndex = this.listFunction.findIndex(f => f.id == func.id)
          if (functionIndex != -1) {
            this.listFunction[functionIndex].name = func.name;
            this.listFunction[functionIndex].feRoute = func.feRoute;
          }
          this.actionModal.showModal = false;
        },
        error: (error) => {
          this.common.errorHandle.handle(error);
        }
      })
  }

  deleteFunction() {
    const endpoint = this.common.getPermission(PermissionName.FunctionManagement.DELETE_FUNCTION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams();
    params = params.set("id", this.currentFunction.id.toString());
    this.common.base.delete(endpoint, params).pipe(
      finalize(()=> {
        this.confirmModal.isLoading = false;
      })
    ).subscribe({
      next: (res) => {
        this.common.snackBar.show(null, res.content, ApiStatus.SUCCESS, 5000)
        this.removeFunction(this.currentFunction.id);
        this.currentFunction = {} as Function
        this.confirmModal.close();
      },
      error: (err) => {
        this.common.errorHandle.handle(err);
      }
    });
  }

  removeFunction(functionId:number){
    this.listFunction = this.listFunction.filter(f => f.id != functionId);
  }

  confirmDelete(fun: Function) {
    this.confirmModal.showConfirmation = true;
    this.currentFunction = { ...fun }
  }




  async toggle(func: Function): Promise<void> {
    if (this.openSectionId === func.id) {
      this.openSectionId = null;
    } else {
      this.openSectionId = func.id;
    }
  }
}

