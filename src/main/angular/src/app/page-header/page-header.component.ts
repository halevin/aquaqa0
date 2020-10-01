import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Globals } from '../app.globals';
import { Utils } from '../app.utils';

@Component({
  selector: 'page-header',
  templateUrl: './page-header.component.html',
  styleUrls: ['./page-header.component.css']
})
export class PageHeaderComponent implements OnInit {

  constructor(public router : Router, public globals : Globals, public utils : Utils) { }

  ngOnInit() {
  }

  logout(){
    console.log( ">>> logout");
    window.sessionStorage.clear();
    this.router.navigate(['home']);
    window.location.href = this.globals.restServerURL+this.globals.logoutURL;
  }

  public getUserFullName(){
    return "Example User";
    // return this.globals.account.firstname + " " +
    // (this.globals.account.initials ? this.globals.account.initials + " " : "") +
    // this.globals.account.lastname;
  }

  public navigateDashboard(){
    this.router.navigate(['dashboard']);
  }

  public navigateSearch(){
    this.router.navigate(['search']);

  }

  public navigateExecblock(){
    this.router.navigate(['execblock']);

  }


}
