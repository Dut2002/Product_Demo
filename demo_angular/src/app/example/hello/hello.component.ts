import { Component } from "@angular/core";

@Component({
  selector: 'hello-app',
  templateUrl: './hello.component.html',
})

export class HelloComponent {
  user = {
    name : "Hà",
    age: 50,
  }

  isTabActive = true

  showArlet(){
    alert("Don't hurt me!")
  }

  authors = [
    {
      id: 1,
      firstName: "Flora",
      lastName: "Twell",
      email: "ftwell0@phoca.cz",
      gender: "Female",
      ipAddress: "99.180.237.33",
    },
    {
      id: 2,
      firstName: "Priscella",
      lastName: "Signe",
      email: "psigne1@berkeley.edu",
      gender: "Female",
      ipAddress: "183.243.228.65",
    },
    // more data
  ];

  selectedTab = 1

  isSelectedTab(tab: number){
    return tab === this.selectedTab
  }

  selectTab(tab: number): void{
    this.selectedTab = tab
  }


}
