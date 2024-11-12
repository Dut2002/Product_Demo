import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PermissionName } from '../../../constant/api.const.urls';
import { RouterUrl } from '../../../constant/app.const.router';
import { FunctionDto } from '../../../model/dto/function-dto';
import { CommonService } from '../../../service/common/common.service';

@Component({
  selector: 'app-role-permission-detail',
  templateUrl: './role-permission-detail.component.html',
  styleUrl: './role-permission-detail.component.scss'
})
export class RolePermissionDetailComponent implements OnInit {

  functions!: FunctionDto[]
  roleId!: number
  constructor(private common: CommonService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.common.setFunctionName(this.route);
    this.route.queryParams.subscribe(params => {
      this.roleId = params['roleId'];
      if (this.roleId) {
        this.loadData();
      } else {
        this.common.router.navigate([RouterUrl.NOT_FOUND]);
      }
    });
  }

  loadData() {
    const endpoint = this.common.getPermission(PermissionName.VIEW_USER_PERMISSION)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams();
    params = params.set("id", this.roleId)
    this.common.base.get(endpoint, params).subscribe({
      next: res => {
        this.functions = res;
        console.log(this.functions);

      },
      error: err => this.common.errorHandle.handle(err)
    })
  }
}
