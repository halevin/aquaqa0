import { Component, OnInit } from '@angular/core';
import {Globals} from '../app.globals';
import { Router, RouterModule } from '@angular/router';
import { AppService } from '../app.service';
import {Utils} from "../app.utils";
import {Qa2DashboardGlobals} from "../app.qa2dashboard.globals";

@Component({
  selector: 'page-header',
  templateUrl: './page-header.component.html',
  styleUrls: ['./page-header.component.css']
})
export class PageHeaderComponent implements OnInit {

  constructor(public globals: Globals, public qa2globals: Qa2DashboardGlobals, public router:Router, private service:AppService, public utils: Utils) { }

  ngOnInit() {
  }

  logout(){
    console.log( ">>> logout");
    window.sessionStorage.clear();
    this.router.navigate(['pi/home']);
    window.location.href = this.globals.restServerURL+this.globals.logoutURL;
  }

  getPiFullName(){
    return this.globals.account.firstname + " " +
    (this.globals.account.initials ? this.globals.account.initials + " " : "") +
    this.globals.account.lastname;
  }
}
