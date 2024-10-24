import { Component, OnInit } from '@angular/core';
import { Role } from '../../../model/role';
import { RoleRes } from '../../../model/dto/role-res';
import { RoleService } from '../../../service/role/role.service';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { ApiStatus } from '../../../constant/api.const.urls';
import { FunctionDto } from '../../../model/dto/function-dto';

@Component({
  selector: 'app-role-permission',
  templateUrl: './role-permission.component.html',
  styleUrl: './role-permission.component.scss'
})
export class RolePermissionComponent implements OnInit {
  openSectionId: number | null = null;
  showModal = false;
  showConfirmation = false;
  currentRole!: Role
  title!: string
  roleList!: RoleRes[]


  constructor(private roleService: RoleService,
    private snackBarService: SnackBarService,
    private errorhanderService: ErrorHandleService
  ) {
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.roleService.getRoles().subscribe({
      next: res => {
        this.roleList = res;
      },
      error: err => {
        this.errorhanderService.handle(err);
      }
    });
  }

  confirmDelete(role: RoleRes) {
    this.showConfirmation = true
    this.currentRole = { ...role }
  }

  closeConfirm() {
    this.showConfirmation = false;
  }

  deleteRole() {
    this.roleService.deleteRole(this.currentRole.id).subscribe({
      next: res => {
        this.snackBarService.show(null, res.content, ApiStatus.SUCCESS, 5000);
        this.roleList = this.roleList.filter(r=>r.id !== this.currentRole.id);
      },
      error: err => this.errorhanderService.handle(err)
    })
    this.closeConfirm();
  }

  toggle(id: number) {
    if (this.openSectionId === id) {
      this.openSectionId = null;
    } else {
      this.openSectionId = id;
    }
  }

  openAddModal() {
    this.currentRole = {} as Role
    this.title = "Add New Role"
    this.showModal = true;
  }

  openUpdateModal(role: Role) {
    this.currentRole = {...role}
    this.showModal = true;
    this.title = "Update Role"
  }

  closeModal() {
    this.showModal = false;
  }

  AddRole() {
    this.loadData();
    this.closeModal();
  }

  UpdateRole(role: Role) {
    const roleIndex = this.roleList.findIndex(r=>r.id == role.id);
    alert(roleIndex)
    if(roleIndex!==-1){
      this.roleList[roleIndex].name = role.name;
    }
    this.closeModal();
  }

  changePermission(list: FunctionDto[], func: FunctionDto){
    const index = list.findIndex(f=> f.id = func.id);
    if(index !== -1){
      list[index] = func;
    }
  }

}
