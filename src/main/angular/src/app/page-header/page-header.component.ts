import { Component, OnInit } from '@angular/core';
import {Globals} from '../app.globals';
import { Router, RouterModule } from '@angular/router';
import { AppService } from '../app.service';
import {Render} from "../app.render";

@Component({
  selector: 'page-header',
  templateUrl: './page-header.component.html',
  styleUrls: ['./page-header.component.css']
})
export class PageHeaderComponent implements OnInit {

  constructor(public globals: Globals, private router:Router, private render : Render, private service:AppService) { }

  ngOnInit() {
  }

  logout(){
    console.log( ">>> logout");
    window.sessionStorage.clear();
    this.router.navigate(['home/pi']);
    window.location.href = this.globals.restServerURL+this.globals.logoutURL;
  }

  getPiFullName(){
    return this.globals.account.firstname + " " +
    (this.globals.account.initials ? this.globals.account.initials + " " : "") +
    this.globals.account.lastname;
  }

  changeMode(event){
    if(event.target.checked){
      this.globals.mode = this.globals.modes.cs;
    } else {
      this.globals.mode = this.globals.modes.pi;
    }
    this.globals.entityId = "";
    this.globals.action = "";
    this.globals.schedBlockId = "";
    this.globals.execBlockId = "";
    this.globals.news = {};
    this.globals.statistics = {};

    if (this.globals.currentDataType == this.globals.dataType.projectList ||
      this.globals.currentDataType == this.globals.dataType.sbList){
      this.globals.projectList = [];
      this.globals.filteredProjectList = [];
      this.globals.sbList = [];
      this.globals.filteredSbList = [];
      this.router.navigateByUrl('/'+this.service.extractAPIFromURL(this.router.url)+'/'+this.globals.mode);
    } else {
      this.router.navigateByUrl('/home/'+this.globals.mode);
    }
    this.service.initHomePageInfo();
  }

  getHelp() {

  }
}
