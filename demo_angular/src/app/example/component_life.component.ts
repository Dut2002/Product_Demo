import { Component, OnInit, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-component-life',
  template: `<p>Example Component</p>`
})
export class ExampleComponent implements OnInit, OnDestroy, OnChanges {

  constructor() {
    console.log('Constructor: Component is being created');
  }

  ngOnChanges(changes: SimpleChanges) {
    console.log('ngOnChanges: Changes detected', changes);
  }

  ngOnInit() {
    console.log('ngOnInit: Component initialized');
  }

  ngOnDestroy() {
    console.log('ngOnDestroy: Component is being destroyed');
  }
}
