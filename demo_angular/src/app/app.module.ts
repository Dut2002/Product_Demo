import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { HelloComponent } from './example/hello/hello.component';
import { ExampleComponent } from './example/component_life.component';
import { ProgressBarComponent } from './example/progress-bar/progress-bar.component';
import { ToggleComponent } from './example/toggle/toggle.component';
import { TypescriptTestComponent } from './example/typescript-test/typescript-test.component';

import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { LoginComponent } from './component/login/login.component';
import { AuthorListComponent } from './component/author/author-list/author-list.component';
import { AuthorDetailComponent } from './component/author/author-detail/author-detail.component';
import { SnackBarComponent } from './component/common/snack-bar/snack-bar.component';
import { authenInterceptor } from './interceptors/auth.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ForbiddenComponent } from './component/common/forbidden/forbidden.component';
import { NotFoundComponent } from './component/common/not-found/not-found.component';
import { ErrorComponent } from './component/common/error/error.component';
import { FunctionComponent } from './component/function/function.component';
import { ConfirmModalComponent } from './component/common/confirm-modal/confirm-modal.component';
import { HeaderComponent } from './component/common/header/header.component';
import { HomeComponent } from './component/common/home/home.component';
import { ProductModalComponent } from './component/product/product-modal/product-modal.component';
import { ProductComponent } from './component/product/product-list/product.component';
import { ProductFilterComponent } from './component/product/product-filter/product-filter.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { SearchDropDownComponent } from './component/common/search-drop-down/search-drop-down.component';
import { ProductVoucherComponent } from './component/product/product-voucher/product-voucher.component';
import { VoucherDetailComponent } from './component/voucher/voucher-detail/voucher-detail.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { MatButtonModule } from '@angular/material/button';
import { MatPseudoCheckboxModule } from '@angular/material/core';
import { MatInputModule } from "@angular/material/input";
import { MatFormFieldModule } from "@angular/material/form-field";


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
    HomeComponent,
    ProductModalComponent,
    ProductFilterComponent,
    SearchDropDownComponent,
    ProductVoucherComponent,
    VoucherDetailComponent,
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
  providers: [provideHttpClient(withInterceptors([authenInterceptor,])), provideAnimationsAsync()],
  bootstrap: [AppComponent,]
})
export class AppModule {

}
