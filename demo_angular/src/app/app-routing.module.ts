import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ErrorComponent } from './component/common/error/error.component';
import { ForbiddenComponent } from './component/common/forbidden/forbidden.component';
import { NotFoundComponent } from './component/common/not-found/not-found.component';
import { LoginComponent } from './component/login/login.component';
import { ProductComponent } from './component/product/product-list/product.component';
import { RolePermissionComponent } from './component/role-permission/role-list/role-permission.component';
import { RolePermissionDetailComponent } from './component/role-permission/role-permission-detail/role-permission-detail.component';
import { RouterUrl } from './constant/app.const.router';
import { FileProgressComponent } from './example/file-progress/file-progress.component';
import { authGuard } from './guard/auth.guard';
import { FunctionComponent } from './component/function/fuction-list/function.component';
import { MySupplierComponent } from './component/my-supplier/container/my-supplier.component';

export const routes: Routes = [


  {
    path: RouterUrl.PRODUCT_MANAGEMENT,
    component: ProductComponent, canActivate: [authGuard]
  },
  {
    path: RouterUrl.USER_ROLE,
    component: RolePermissionComponent, canActivate: [authGuard]
  },
  {
    path: RouterUrl.FUNCTION_MANAGEMENT,
    component: FunctionComponent, canActivate: [authGuard]
  },
  {
    path: RouterUrl.USER_PERMISION,
    component: RolePermissionDetailComponent, canActivate: [authGuard]
  },
  {
    path: RouterUrl.MY_SUPPLIER,
    component: MySupplierComponent, canActivate: [authGuard]
  },
  {
    path: RouterUrl.FORBIDDEN,
    component: ForbiddenComponent
  },
  {
    path: RouterUrl.NOT_FOUND,
    component: NotFoundComponent
  },
  {
    path: RouterUrl.ERROR,
    component: ErrorComponent
  },
  {
    path: RouterUrl.FILE_PROGRESS,
    component: FileProgressComponent
  },
  {
    path: '',
    redirectTo: RouterUrl.LOG_IN,
    pathMatch: 'full'
  }, // Redirect đến login
  {
    path: RouterUrl.LOG_IN,
    component: LoginComponent
  },
  {
    path: '**',
    component: NotFoundComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
