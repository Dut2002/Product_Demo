import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ProductFilter } from '../../../model/dto/product-filter';
import { SearchBoxDto } from '../../../model/dto/search-box-dto';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { ProductService } from '../../../service/product/product.service';

@Component({
  selector: 'app-product-filter',
  templateUrl: './product-filter.component.html',
  styleUrl: './product-filter.component.scss'
})
export class ProductFilterComponent implements OnInit{

  @Input() filter = {} as ProductFilter;
  @Output() applyEvent = new EventEmitter<ProductFilter>();
  @Output() resetEvent = new EventEmitter<void>();

  categorySearchBox: SearchBoxDto[] = []
  supplierSearchBox: SearchBoxDto[] = []
  customerSearchBox: SearchBoxDto[] = []

  ngOnInit(): void {
    this.loadCategory(null)
    this.loadCustomer(null)
    this.loadSupplier(null)
  }

  constructor(private productService: ProductService,
    private errorHandle: ErrorHandleService,
  ) { }

  loadCategory(name: string|null){
    this.productService.getCategoryBox(name).subscribe({
      next: (response) =>{
        this.categorySearchBox = response;
      },
      error: (err) => {
        this.errorHandle.handle(err);
      }
    }
    )
  }

  loadCustomer(name: string|null){
    this.productService.getCustomerBox(name).subscribe({
      next: (response) =>{
        this.customerSearchBox = response;
      },
      error: (err) => {
        this.errorHandle.handle(err);
      }
    }
    )
  }

  loadSupplier(name: string|null){
    this.productService.getSupplierBox(name).subscribe({
      next: (response) =>{
        this.supplierSearchBox = response;
      },
      error: (err) => {
        this.errorHandle.handle(err);
      }
    }
    )
  }


  onCategorySelected(id: number|null)
  {
    this.filter.categoryId = id;
  }

  onSupplierSelected(id: number|null)
  {
    this.filter.supplierId = id;
  }

  onCustomerSelected(id: number|null)
  {
    this.filter.customerId = id;
  }


  onApply(){
    this.filter.pageNum = 1;
    this.applyEvent.emit(this.filter);
  }

  onReset(){
    this.resetEvent.emit();
  }

  changePrice(isMax:boolean){
    if(this.filter.maxPrice && this.filter.minPrice && this.filter.minPrice >= this.filter.maxPrice){
      if(isMax) {
        this.filter.maxPrice = this.filter.minPrice
      }else {this.filter.minPrice = this.filter.maxPrice}
    }
  }

  changeQuality(isMax:boolean){
    if(this.filter.maxQuantity && this.filter.minQuantity && this.filter.minQuantity >= this.filter.maxQuantity){
      if(isMax) this.filter.maxQuantity = this.filter.minQuantity
      else this.filter.minQuantity = this.filter.maxQuantity
    }
  }

}
