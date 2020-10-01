import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { AppService } from './app.service';
import { Globals } from './app.globals';
import { Router, ActivatedRoute } from '@angular/router';
import { Emitters } from './app.emitters';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'aquaqa0';
  sidebarDisplayed = true;
  progressDisplayed = false;
  class = "active";
  subscriptionHideSideBar: any;
  subscriptionExpandSideBar: any;


  constructor(private cdr: ChangeDetectorRef, private appService : AppService, private globals : Globals, 
    private route: ActivatedRoute, private emitters: Emitters){}

  ngOnInit(): void {
    this.subscriptionHideSideBar = this.emitters.getHideSideBarEmitter()
    .subscribe(() =>{
        this.sidebarDisplayed = false;
        this.class = "";
      }
    );
    this.subscriptionExpandSideBar = this.emitters.getExpandSideBarEmitter()
    .subscribe(() =>{
        this.sidebarDisplayed = true;
        this.class = "active";
      }
    );

    this.appService.startup();
}

onPressed(sidebarDispl: boolean){
  this.sidebarDisplayed = sidebarDispl;
  if (this.sidebarDisplayed){
    this.class = "active";
  } else {
    this.class = "";
  }
}

  
}
