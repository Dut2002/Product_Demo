import { NgModule } from '@angular/core';
import { RouterUrl } from './constant/app.const.router';
import { LoginComponent } from './component/login/login.component';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './guard/auth.guard';
import { ForbiddenComponent } from './component/common/forbidden/forbidden.component';
import { NotFoundComponent } from './component/common/not-found/not-found.component';
import { ErrorComponent } from './component/common/error/error.component';
import { FunctionComponent } from './component/function/function.component';
import { HomeComponent } from './component/common/home/home.component';
import { ProductComponent } from './component/product/product-list/product.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: RouterUrl.LOG_IN.path,
    pathMatch: 'full'
  }, // Redirect đến login
  {
    path: RouterUrl.LOG_IN.path,
    component: LoginComponent
  },
  {
    path: RouterUrl.GET_PRODUCTS.path,
    component: ProductComponent,  canActivate: [authGuard], data: {authority: RouterUrl.GET_PRODUCTS.name}
  },
  {
    path: RouterUrl.GET_FUNCTIONS.path,
    component: FunctionComponent,  canActivate: [authGuard], data: {authority: RouterUrl.GET_FUNCTIONS.name}
  },
  {
    path: RouterUrl.FORBIDDEN.path,
    component: ForbiddenComponent
  },
  {
    path: RouterUrl.NOT_FOUND.path,
    component: NotFoundComponent
  },
  {
    path: RouterUrl.ERROR.path,
    component: ErrorComponent
  },
  {
    path: RouterUrl.BASE_URL.path,
    component: HomeComponent
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
