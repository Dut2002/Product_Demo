import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ExampleComponent } from './example/component_life.component';
import { HelloComponent } from './example/hello/hello.component';
import { ProgressBarComponent } from './example/progress-bar/progress-bar.component';
import { ToggleComponent } from './example/toggle/toggle.component';
import { TypescriptTestComponent } from './example/typescript-test/typescript-test.component';

import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { MatButtonModule } from '@angular/material/button';
import { MatPseudoCheckboxModule } from '@angular/material/core';
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { NgSelectModule } from '@ng-select/ng-select';
import { AuthorDetailComponent } from './component/author/author-detail/author-detail.component';
import { AuthorListComponent } from './component/author/author-list/author-list.component';
import { ConfirmModalComponent } from './component/common/confirm-modal/confirm-modal.component';
import { ErrorComponent } from './component/common/error/error.component';
import { ForbiddenComponent } from './component/common/forbidden/forbidden.component';
import { HeaderComponent } from './component/common/header/header.component';
import { NotFoundComponent } from './component/common/not-found/not-found.component';
import { SearchDropDownComponent } from './component/common/search-drop-down/search-drop-down.component';
import { SnackBarComponent } from './component/common/snack-bar/snack-bar.component';
import { FunctionComponent } from './component/function/fuction-list/function.component';
import { FunctionDetailComponent } from './component/function/function-detail/function-detail.component';
import { FunctionModalComponent } from './component/function/function-modal/function-modal.component';
import { PermissionAddComponent } from './component/function/permission-add/permission-add.component';
import { PermissionDetailsComponent } from './component/function/permission-details/permission-details.component';
import { LoginComponent } from './component/login/login.component';
import { ProductFilterComponent } from './component/product/product-filter/product-filter.component';
import { ProductImportComponent } from './component/product/product-import/product-import.component';
import { ProductComponent } from './component/product/product-list/product.component';
import { ProductModalComponent } from './component/product/product-modal/product-modal.component';
import { ProductVoucherComponent } from './component/product/product-voucher/product-voucher.component';
import { RoleFunctionDetailsComponent } from './component/role-permission/role-function-details/role-function-details.component';
import { RolePermissionComponent } from './component/role-permission/role-list/role-permission.component';
import { RoleModalComponent } from './component/role-permission/role-modal/role-modal.component';
import { RolePermissionDetailComponent } from './component/role-permission/role-permission-detail/role-permission-detail.component';
import { VoucherDetailComponent } from './component/voucher/voucher-detail/voucher-detail.component';
import { FileProgressComponent } from './example/file-progress/file-progress.component';
import { authenInterceptor } from './interceptors/auth.interceptor';
import { MySupplierComponent } from './component/my-supplier/container/my-supplier.component';
import { MySupplierHomeComponent } from './component/my-supplier/my-supplier-home/my-supplier-home.component';
import { MySupplierRequestComponent } from './component/my-supplier/my-supplier-request/my-supplier-request.component';
import { MySupplierRequestDetailComponent } from './component/my-supplier/my-supplier-request-detail/my-supplier-request-detail.component';
import { SupplierManagerComponent } from './component/supplier/supplier-manager/supplier-manager.component';
import { SupplierRequestListComponent } from './component/supplier/supplier-request-list/supplier-request-list.component';
import { SupplierRequestDetailsComponent } from './component/supplier/supplier-request-details/supplier-request-details.component';
import { HomeComponent } from './component/common/home/home.component';
import { UserManagementComponent } from './component/user/user-management/user-management.component';

@NgModule({
  declarations: [
    AppComponent,
    HelloComponent,
    ExampleComponent,
    ProgressBarComponent,
    AuthorListComponent,
    AuthorDetailComponent,
    ToggleComponent,
    TypescriptTestComponent,
    ProductComponent,
    LoginComponent,
    SnackBarComponent,
    ForbiddenComponent,
    NotFoundComponent,
    ErrorComponent,
    FunctionComponent,
    ConfirmModalComponent,
    HeaderComponent,
    ProductModalComponent,
    ProductFilterComponent,
    SearchDropDownComponent,
    ProductVoucherComponent,
    VoucherDetailComponent,
    FunctionModalComponent,
    FunctionDetailComponent,
    FileProgressComponent,
    PermissionDetailsComponent,
    PermissionAddComponent,
    RolePermissionComponent,
    RoleModalComponent,
    RolePermissionDetailComponent,
    ProductImportComponent,
    RoleFunctionDetailsComponent,
    MySupplierComponent,
    MySupplierHomeComponent,
    MySupplierRequestComponent,
    MySupplierRequestDetailComponent,
    SupplierManagerComponent,
    SupplierRequestListComponent,
    SupplierRequestDetailsComponent,
    HomeComponent,
    UserManagementComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    NgSelectModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatPseudoCheckboxModule,
    MatButtonModule,
  ],
  providers: [
    provideHttpClient(withInterceptors([authenInterceptor,])),
    provideAnimationsAsync(),
  ],
  bootstrap: [AppComponent,]
})
export class AppModule {

}
