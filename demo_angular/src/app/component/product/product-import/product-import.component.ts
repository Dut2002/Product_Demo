import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { saveAs } from 'file-saver';
import { finalize } from 'rxjs';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { AssetsUrl } from '../../../constant/assets.const.urls';
import { ProductImport } from '../../../model/dto/product-import';
import { HeaderMapping, ImportType, ParseOptions, ParseResult } from '../../../model/import/import-dto';
import { ValidationConfig } from '../../../model/import/validation-dto';
import { CommonService } from '../../../service/common/common.service';
import { ExcelParse } from '../../../service/excel-parse.service';
import { Pattern } from '../../../constant/paterns.const';

@Component({
  selector: 'app-product-import',
  templateUrl: './product-import.component.html',
  styleUrl: './product-import.component.scss'
})
export class ProductImportComponent implements OnInit {
  ImportType = ImportType

  @Input() common!: CommonService;
  @Output() successEvent = new EventEmitter<void>();

  showModal = false;
  file: File | null = null;
  allowedTypes = [
    'application/vnd.ms-excel',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  ];
  viewError: boolean = false;
  typeImport: ImportType = ImportType.ADD;
  disableButton = true;
  isLoading = false;
  listProduct!: ProductImport[];
  parseResult!: ParseResult | null;

  options!: ParseOptions;

  headerMappings: HeaderMapping[] = [
    {
      excelHeader: 'Id',
      fieldName: 'id',
      required: [false, true, true],
      unique: [false, true, true],
      transform: (value: any) => Number(value)
    },
    {
      excelHeader: 'Name',
      fieldName: 'name',
      required: [true, true, false],
      unique: [true, true, false],
    },
    {
      excelHeader: 'Year Making',
      fieldName: 'yearMaking',
      required: [true, true, false],
      unique: [false, false, false],
      transform: (value: any) => Number(value)
    },
    {
      excelHeader: 'Price',
      fieldName: 'price',
      required: [true, true, false],
      unique: [false, false, false],
      transform: (value: any) => Number(value)
    },
    {
      excelHeader: 'Quantity',
      fieldName: 'quantity',
      required: [true, true, false],
      unique: [false, false, false],
      transform: (value: any) => Number(value)
    },
    {
      excelHeader: 'Expire Date',
      fieldName: 'expireDate',
      required: [false, false, false],
      unique: [false, false, false],
      transform: (value: any) => {
        if (value instanceof Date) return value;
        if (typeof value === 'number') return new Date(Math.round((value - 25569) * 86400 * 1000));
        return new Date(value);
      }
    },
    {
      excelHeader: 'Category',
      fieldName: 'categoryName',
      required: [true, true, false],
      unique: [false, false, false],
    },
    {
      excelHeader: 'Supplier',
      fieldName: 'supplierName',
      required: [true, true, false],
      unique: [false, false, false],
    }
  ];


  @ViewChild('ImportButton', { static: false }) importButton!: ElementRef<HTMLButtonElement>;

  private validationConfig: ValidationConfig = {
    id: {
      rules: [
        { type: 'numberic', message: 'Id là kiểu số' }
      ]
    },
    name: {
      rules: [
        { type: 'maxLength', params: 100, message: 'Tên không được quá 100 ký tự' },
        { type: 'pattern', params: Pattern.vietnamesePattern, message: 'Tên chỉ được chứa chữ cái và khoảng trắng' }
      ]
    },
    yearMaking: {
      rules: [
        { type: 'minValue', params: 1955, message: "Sản phẩm từ năm 1955 trở về sau" }
      ]
    },
    price: {
      rules: [
        { type: 'minValue', params: 0, message: "Giá sản phẩm không thể có giá trị âm" }
      ]
    },
    quantity: {
      rules: [
        { type: 'minValue', params: 0, message: "Số lượng sản phẩm không thể có giá trị âm" }
      ]
    },
    expireDate: {
      rules: [
        { type: 'date' }
      ]
    },
    categoryName: {
      rules: [
      ]
    },
    supplierName: {
      rules: [
      ]
    },
    typeImport: {
      rules: [
        { type: 'numberic' }
      ]
    }
  }

  ngOnInit(): void {
    this.file = null;
    this.listProduct = [];
    this.parseResult = null;
  }

  onChangeImport() {
    if (this.file) {
      this.isLoading = true;
      this.listProduct = [];
      this.parseResult = null;
      if (this.file) {
        const fileReader = new FileReader();
        fileReader.onload = async (e) => {
          const arrayBuffer = e.target?.result;
          let headerMaps: HeaderMapping[] = [];
          switch (this.typeImport) {
            case ImportType.ADD: {
              headerMaps = this.headerMappings.filter(h => h.excelHeader !== 'Id');
              break;
            }
            case ImportType.UPDATE: {
              headerMaps = [...this.headerMappings]; // Sao chép mảng
              break;
            }
            case ImportType.DELETE: {
              headerMaps = this.headerMappings.filter(h => h.excelHeader === 'Id');
              break;
            }
          }

          this.options = {
            headerRow: 1,
            strictMapping: true,
            typeImport: this.typeImport,
          };

          const excelParse = new ExcelParse(headerMaps, this.validationConfig, this.options);


          this.parseResult = await excelParse.parseExcel(arrayBuffer);
          // Xử lý kết quả
          this.listProduct = this.parseResult.listData as ProductImport[]

          if (this.parseResult.success) {
            //validate file trên fe thành công gửi sang be validate
            this.validateData(false);
          } else if (this.parseResult.globalErrors?.length === 0) {
            //validate file trên fe có 1 số data lỗi, cần xác nhận tiếp tục
            this.common.snackBar.show(null, 'Một số dữ liệu bảng lỗi', ApiStatus.ERROR, 5000);
            this.disableButton = false;
            this.isLoading = false;
            this.importButton.nativeElement.onclick = null;
            this.importButton.nativeElement.addEventListener('click', () => this.validateData(true));
          } else {
            // xử lý thông báo lỗi validate file trên fe
            this.common.snackBar.show(null, 'Lỗi File Template', ApiStatus.ERROR, 5000);
            this.isLoading = false;
          }
        }
        fileReader.readAsArrayBuffer(this.file); // Gọi onload
      }
    }
  }

  onFileChange(event: any, fileInput: HTMLInputElement) {
    this.file = event.target.files[0];
    this.onChangeImport();
    // if(this.file)saveAs(this.file,this.file?.name);
    fileInput.value = '';
  }

  validateData(skipError: boolean) {
    this.isLoading = true;
    this.disableButton = true;
    const endpoint = this.common.getPermission(PermissionName.ProductManagement.UPLOAD_PRODUCT)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }

    const formData = new FormData();
    formData.append('file', this.file!);

    Object.keys(this.options).forEach(key => {
      const value = this.options[key as keyof ParseOptions];
      if (value !== undefined) {
        if (Array.isArray(value)) {
          formData.append(key, JSON.stringify(value));
        } else {
          formData.append(key, value.toString());
        }
      } else {
        formData.append(key, '');
      }
    });

    this.common.base.uploadFile(endpoint, formData)
      .subscribe({
        next: res => {
          this.parseResult = res;
          this.listProduct = res.listData as ProductImport[];
          if (this.parseResult?.success === true) {
            this.common.snackBar.show(null, 'Import Product Successfull!', ApiStatus.SUCCESS, 5000);
            this.isLoading = false;
            this.successEvent.emit();
          }
          else if (this.parseResult?.globalErrors?.length === 0) {
            if (skipError) {
              this.importData();
            } else {
              //một số data lỗi, cần xác nhận tiếp tục gọi be xử lý
              this.common.snackBar.show(null, 'Một số dữ liệu bảng bị lỗi trên Server', ApiStatus.ERROR, 5000);
              this.disableButton = false;
              this.isLoading = false;
              this.importButton.nativeElement.onclick = null;
              this.importButton.nativeElement.addEventListener('click', () => this.importData());
            }
          } else {
            this.common.snackBar.show(null, 'Lỗi validate File trên Server', ApiStatus.ERROR, 5000);
            this.isLoading = false;
          }

        },
        error: err => this.common.errorHandle.handle(err)
      })
  }

  importData() {
    this.disableButton = true;
    this.isLoading = true;

    const endpoint = this.common.getPermission(PermissionName.ProductManagement.IMPORT_PRODUCT)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.post(endpoint, this.parseResult)
      .pipe(
        finalize(() => {
          this.isLoading = false;
        })
      )
      .subscribe({
        next: res => {
          this.parseResult = res;
          this.listProduct = res.listData as ProductImport[];
          if (this.parseResult?.success) {
            this.common.snackBar.show(null, 'Import Product Successfull!', ApiStatus.SUCCESS, 5000);
            this.successEvent.emit();
          } else {
            this.common.snackBar.show(null, 'Import Product Failed!', ApiStatus.ERROR, 5000);
          }
        },
        error: err => this.common.errorHandle.handle(err)
      })
  }


  onClose() {
    this.showModal = false;
  }

  isValid() {
    return this.file && this.parseResult && (this.parseResult.success || this.viewError) && this.listProduct.length > 0;
  }

  downloadTemplate() {
    saveAs(AssetsUrl.PRODUCT_IMPORT_TEMPLATE);
  }
}



