import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Globals } from '../app.globals';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(public router:Router, private route : ActivatedRoute, private globals : Globals) { }

  ngOnInit() {

    this.route
    .queryParams
    .subscribe( params => {
      console.log(" Params : " + JSON.stringify(params));
      if ( params != null && params.ebuid != null) {
        this.globals.currentExecBlockUID = params.ebuid;
      }

    })

  }

}
