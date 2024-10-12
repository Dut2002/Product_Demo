import { Component, OnInit } from '@angular/core';
import { Function } from '../../model/function';
import { FunctionService } from '../../service/function/function.service';
import { RoleService } from '../../service/role/role.service';
import { SnackBarService } from '../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../service/error-handle/error-handle.service';
import { Role } from '../../model/role';
import { ApiStatus, ApiUrls } from '../../constant/api.const.urls';
import { FunctionDto } from '../../model/dto/function-dto';
import { RoleAccess } from '../../model/dto/role-access';

@Component({
  selector: 'app-function',
  templateUrl: './function.component.html',
  styleUrl: './function.component.scss'
})
export class FunctionComponent implements OnInit {
  showModal = false;
  showConfirmation = false;
  roles: Role[] = [];

  currentForm: FunctionDto = {
    id: 0,
    name: '',
    endPoint: '',
    roleAccesses: []
  };

  listFunction: FunctionDto[] = []
  saveFunc = {} as FunctionDto


  constructor(private functionService: FunctionService,
    private roleService: RoleService,
    private snackBarService: SnackBarService,
    private errorHandleService: ErrorHandleService,
  ) { }
  ngOnInit(): void {
    this.getRoles();
    this.loadList();
  }

  openAddModal() {
    this.showModal = true;
  }

  loadList() {
    this.functionService.getFunctions().subscribe({
      next: (response) => {
        this.listFunction = response;
      },
      error: (err) => {
        this.errorHandleService.handle(err);
      }
    })
  }

  getRoles() {
    this.roleService.getRoles().subscribe({
      next: (response) => {
        this.roles = response;
        this.initRoleAccess();
      },
      error: (error) => {
        this.errorHandleService.handle(error)
      }
    })
  }

  initRoleAccess() {
    this.currentForm.roleAccesses = this.roles.map(role => ({
      roleId: role.id,
      roleName: role.name.replace('ROLE_', ''),
      permission: false // Default to false
    }));
  }

  saveFunction() {
    this.functionService.addFunction(this.currentForm).subscribe({
      next: (response) => {
        this.snackBarService.show(null, response.content, ApiStatus.SUCCESS, 5000);
        this.listFunction.push(this.currentForm)
        this.closeModal();
      },
      error: (error) => {
        this.errorHandleService.handle(error);
      },
      complete: () => {
        console.log("Add Function request completed!");
      }
    })
  }

  refreshAddForm() {
    this.currentForm.name = '';
    this.currentForm.endPoint = ''
    this.currentForm.roleAccesses.forEach(roleAccess => {
      roleAccess.permission = false;
    });
  }

  closeModal() {
    this.showModal = false;
    this.currentForm = {} as FunctionDto;
  }

  confirmDelete(fun: Function) {
    this.showModal = true;
  }

  deleteFunction() {
    this.showConfirmation = false;
  }

  cancleConfirm() {
    this.showConfirmation = false
  }

  openSectionId: number | null = null;

  async toggle(func: FunctionDto): Promise<void> {
    if (this.openSectionId === func.id) {
      this.openSectionId = null;
      this.saveFunc = {} as FunctionDto
    } else {
      if (!func.roleAccesses) {
        const roleAccesses = await this.fetchRoleAccess(func.id).catch(err => {
          console.error('Error loading role access:', err);
          return null; // Hoặc xử lý khác tùy ý
        });
        func.roleAccesses = roleAccesses;
      }
      this.saveFunc = { ...func };
      this.openSectionId = func.id;
    }
  }

  isOpen(sectionId: number): boolean {
    return this.openSectionId === sectionId;
  }

  fetchRoleAccess(funcId: number): Promise<any> {
    return new Promise((resolve, reject) => {
      this.functionService.getFunctionDetail(funcId).subscribe({
        next: (response) => {
          resolve(response); // Giả sử response có thuộc tính roleAccesses
        },
        error: (err) => {
          this.errorHandleService.handle(err);
          reject(err);
        }
      });
    });
  }

  onUpdateFunc(index: number) {


  }

  onChangeFunc(index: number) {

  }
}
