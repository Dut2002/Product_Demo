import { Component } from '@angular/core';

@Component({
  selector: 'app-search-drop-down',
  templateUrl: './search-drop-down.component.html',
  styleUrl: './search-drop-down.component.scss'
})
export class SearchDropDownComponent {
  search = '';
  showDropdown = false;
  categories = ['Category 1', 'Category 2', 'Category 3', 'Category 4'];
  filteredCategories = this.categories;

  filterCategories(value: string) {
    this.filteredCategories = this.categories.filter(category =>
      category.toLowerCase().includes(value.toLowerCase())
    );
  }

  selectCategory(category: string) {
    // Xử lý khi chọn category
    console.log('Selected category:', category);
    this.showDropdown = false; // Ẩn dropdown sau khi chọn
  }

  hideDropdown() {
    setTimeout(() => this.showDropdown = false, 200); // Delay để cho phép click vào dropdown
  }
}
