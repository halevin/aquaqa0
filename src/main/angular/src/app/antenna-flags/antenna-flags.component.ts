import { Component, OnInit } from '@angular/core';
import { Emitters } from '../app.emitters';
import { Globals } from '../app.globals';
import { AppService } from '../app.service';

@Component({
  selector: 'antenna-flags',
  templateUrl: './antenna-flags.component.html',
  styleUrls: ['./antenna-flags.component.css']
})
export class AntennaFlagsComponent implements OnInit {

  progressDisplayed : boolean = true;

  constructor(public globals : Globals, private service : AppService, private emitters : Emitters) { }

  ngOnInit() {

    if ( this.globals.antennaFlags == null || this.globals.antennaFlags.length == 0 ) {
      this.service.getAntennaFlags(this.globals.currentExecBlockUID);
    } else {
      this.progressDisplayed = false;
    }

    let self = this;
    this.emitters.getAntennaFlagsLoadingEmitter().subscribe(()=>{
      self.progressDisplayed = false;
    })

  }

}
