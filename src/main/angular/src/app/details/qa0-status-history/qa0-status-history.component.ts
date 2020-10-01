import { Component, OnInit } from '@angular/core';
import { Globals } from 'src/app/app.globals';
import { AppService } from 'src/app/app.service';

@Component({
  selector: 'qa0-status-history',
  templateUrl: './qa0-status-history.component.html',
  styleUrls: ['./qa0-status-history.component.css']
})
export class Qa0StatusHistoryComponent implements OnInit {

  constructor( private service : AppService, public globals : Globals) { }

  ngOnInit() {

    this.service.getQA0StatusHistory( this.globals.currentExecBlockUID );

  }

}
