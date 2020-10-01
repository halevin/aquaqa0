import { Component, OnInit } from '@angular/core';
import { Emitters } from 'src/app/app.emitters';
import { Globals } from 'src/app/app.globals';
import { AppService } from 'src/app/app.service';

@Component({
  selector: 'app-atmosphere',
  templateUrl: './atmosphere.component.html',
  styleUrls: ['./atmosphere.component.css']
})
export class AtmosphereComponent implements OnInit {

  public spinnerVisible = true;
  public data: any = [];


  constructor( private globals : Globals, private service : AppService, private emitters : Emitters) { }

  ngOnInit() {

    console.log("Atmosphere initialized for " + this.globals.currentExecBlockUID);

    this.service.getAtmosphere(this.globals.currentExecBlockUID);

    let self = this;
    this.emitters.getAtmosphereLoadedEmitter().subscribe(item => {
      self.data = item;
      console.log("Data to plot " + JSON.stringify(self.data));
      this.spinnerVisible = false;
      // Highcharts.chart('container', this.getOptions(self.data, self.offSourceData, self.rectangle, self.expectedOff, 'Coverages for ' + self.plotTitle));
  
    }
  )


  }

}
