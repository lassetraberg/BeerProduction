import { Component, OnInit, OnDestroy } from '@angular/core';
import { DataService } from 'src/app/shared/services/data.service';
import { ScadaScreen } from 'src/app/shared/models/scadaScreen';

@Component({
  selector: 'app-screen',
  templateUrl: './screen.component.html',
  styleUrls: ['./screen.component.scss']
})
export class ScreenComponent implements OnInit, OnDestroy {
  timer;
  time: 0;
  model: ScadaScreen = {batchID: null, orderNumber: null, beerType: null, produced: null, accepted: null, defective: null, temperature: null, humidity: null, vibration: null, productAmount: null, machineSpeed: null};
  
  constructor(private data: DataService) { }

  ngOnInit() {
    this.time = 0;
    this.timer = setInterval(() => {         //replaced function() by ()=>
      this.time += 1;
      console.log('time: ' + this.time); // just testing if it is working
      this.data.viewScreen().subscribe(res => {
        if (res["BatchInfo"] != null && res["State"] == "EXECUTE") {
          this.updateScreen(res);
        }
      });
    }, 500);
  }

  ngOnDestroy () {
    clearInterval(this.timer);
  }

  updateScreen (res) {
    this.model.batchID = res["BatchOrder"]["batchId"];
    this.model.orderNumber = res["BatchInfo"]["orderNumber"];
    this.model.beerType = res["BatchInfo"]["beerName"];
    this.model.produced = res["BatchData"]["produced"];
    this.model.accepted = res["BatchData"]["acceptable"];
    this.model.defective = res["BatchData"]["defect"];
    this.model.temperature = res["Measurements"]["temperature"];
    this.model.humidity = res["Measurements"]["humidity"];
    this.model.vibration = res["Measurements"]["vibration"];
    this.model.productAmount = res["BatchOrder"]["amountToProduce"];
    this.model.machineSpeed = res["BatchOrder"]["productsPerMinute"];
  }
  
  manageProduction (action: string) {
    console.log('Manage production: ' + action);
    
    this.data.manageProduction(action).toPromise();

  }



}
