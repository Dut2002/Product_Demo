import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { SearchBoxDto } from '../../../model/dto/search-box-dto';

@Component({
  selector: 'app-search-drop-down',
  templateUrl: './search-drop-down.component.html',
  styleUrl: './search-drop-down.component.scss'
})
export class SearchDropDownComponent {
  search = '';
  showDropdown = false;
  @Input() id: number|null = null
  @Input() placeHolder = 'Select option'
  @Input() list = [] as SearchBoxDto[];
  @Input() isRequired = false;
  @Output() loadEvent = new EventEmitter<string>()
  @Output() selectEvent = new EventEmitter<number|null>()



  ngOnChanges(changes: SimpleChanges) {
    if (changes['id'] && changes['id'].currentValue !== changes['id'].previousValue) {
      setTimeout(()=>this.updateSearchValue(),200);
    }
  }

  updateSearchValue() {
    if(this.id!=null){
      console.log(this.list);

      const option = this.list.find(opt => opt.id === this.id);
      this.search = option ? option.name : ''; // Cập nhật giá trị input
    }else{
      this.search = '';
    }

  }

  selectOption(option: SearchBoxDto|null) {
    if(option){
      this.search = option.name
      this.selectEvent.emit(option.id)
    }else{
      this.search = ''
      this.selectEvent.emit(null)
    }
    this.hideDropdown()
  }

  hideDropdown() {
    setTimeout(() => this.showDropdown = false, 200);
  }

  openDropdown(){
    this.showDropdown = true;
    this.list = [] as SearchBoxDto[];
    this.loadList();
  }

  loadList(){
    this.loadEvent.emit(this.search);
  }
}
