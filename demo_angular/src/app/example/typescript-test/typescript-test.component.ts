import { Component } from '@angular/core';

type StringOrNumber = string|number;

@Component({
  selector: 'app-typescript-test',
  templateUrl: './typescript-test.component.html',
  styleUrl: './typescript-test.component.scss'
})


export class TypescriptTestComponent {


  listen(port: StringOrNumber): void {
    // if (typeof port === "string") {
    //   port = parseInt(port, 10);
    // }
    console.log(port)
  }

  doSomething(): StringOrNumber {
    return "hello"
    return 123
    // return true
  }
}
