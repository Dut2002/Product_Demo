import { Component, EventEmitter, Input, OnInit, Output, SimpleChange } from '@angular/core';
import { ProductFilter } from '../../../model/dto/product-filter';
import { SearchBoxDto } from '../../../model/dto/search-box-dto';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
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

  selectedCategoryName = '';

  categorySearchBox: SearchBoxDto[] = []
  supplierSearchBox: SearchBoxDto[] = []
  customerSearchBox: SearchBoxDto[] = []

  ngOnInit(): void {


  }

  constructor(private productService: ProductService,
    private snackBarService: SnackBarService,
    private errorHandle: ErrorHandleService,
  ) { }

  changeCategory(change: SimpleChange){

  }

  loadCategory(name: string){
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


  onCategorySelected(option: any)
  {
    console.log(option);

    this.filter.categoryId = option.id;
    this.selectedCategoryName = option.name;
  }


  onApply(){
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
