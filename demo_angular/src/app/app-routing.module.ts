import { NgModule } from '@angular/core';
import { RouterUrl } from './constant/app.const.router';
import { LoginComponent } from './component/login/login.component';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './guard/auth.guard';
import { ForbiddenComponent } from './component/common/forbidden/forbidden.component';
import { NotFoundComponent } from './component/common/not-found/not-found.component';
import { ErrorComponent } from './component/common/error/error.component';
import { FunctionComponent } from './component/function/fuction-list/function.component';
import { HomeComponent } from './component/common/home/home.component';
import { ProductComponent } from './component/product/product-list/product.component';
import { FileProgressComponent } from './example/file-progress/file-progress.component';
import { RolePermissionComponent } from './component/role-permission/role-list/role-permission.component';

export const routes: Routes = [


  {
    path: RouterUrl.VIEW_PRODUCTS,
    component: ProductComponent,  canActivate: [authGuard]
  },
  {
    path: RouterUrl.VIEW_ROLES,
    component: RolePermissionComponent,  canActivate: [authGuard]
  },
  {
    path: RouterUrl.VIEW_FUNCTIONS,
    component: FunctionComponent,  canActivate: [authGuard]
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
    path: RouterUrl.HOME,
    component: HomeComponent, canActivate: [authGuard]
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
