import { Component, Input, OnChanges, OnInit, SimpleChange, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-progress-bar',
  templateUrl: './progress-bar.component.html',
  styleUrls: ['./progress-bar.component.scss']
})
export class ProgressBarComponent implements OnInit, OnChanges{
  @Input() backgroundColor: string | undefined;
  @Input() progressColor: string | undefined;
  @Input() progress = 0;

  interval: any;

  constructor() {}

  ngOnInit() {
  }

  decreaseProgress(){
    this.progress-=1
  }

  increaseProgress(){
    this.progress+=1
  }

  startIncreasing() {
    this.increaseProgress();
    this.interval = setInterval(() => this.increaseProgress(), 100); // Gọi hàm mỗi 100ms
  }

  startDecreasing() {
    this.decreaseProgress();
    this.interval = setInterval(() => this.decreaseProgress(), 100); // Gọi hàm mỗi 100ms
  }

  stopChanging() {
    clearInterval(this.interval); // Dừng gọi hàm
  }

  ngOnChanges(changes: SimpleChanges): void {
    if("progress" in changes) {
      if(typeof changes["progress"].currentValue === "number" ){
        const progress = Number(changes["progress"].currentValue)
        if(Number.isNaN(progress) || progress <= 0 ){
          this.progress = 0
        }else if(progress >= 100){
          this.progress = 100
          alert("Progress Complete!")
        }else{
          this.progress = progress
        }
      }
    }
  }
}
