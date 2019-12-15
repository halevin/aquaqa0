import { Component, OnInit, ChangeDetectorRef  } from '@angular/core';
import {AppService} from './app.service';
import {Globals} from './app.globals';
import { ActivatedRoute } from '@angular/router';
import {Utils} from "./app.utils";
import {Qa2DashboardGlobals} from "./app.qa2dashboard.globals";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  progressDisplayed = false;
  class = "active";
  subscriptionOn: any;
  subscriptionOff: any;

  constructor(private cdr: ChangeDetectorRef, private appService: AppService, private globals: Globals, public qa2globals: Qa2DashboardGlobals, private route: ActivatedRoute, public utils: Utils){
	}

	ngOnInit():void {

			this.subscriptionOn = this.appService.getLoadingOnEmitter()
			.subscribe(item =>{
          if ((this.globals.api !== "project-list" && this.globals.api !== "schedblock-list") ){
            console.log(" =========== progressDisplayed" + this.progressDisplayed);
            Promise.resolve(null).then(() => this.progressDisplayed = true);
          }
        }
      );
			this.subscriptionOff = this.appService.getLoadingOffEmitter()
			.subscribe(item =>{
        console.log(" =========== progressDisplayed" + this.progressDisplayed);
        Promise.resolve(null).then(() => this.progressDisplayed = false);
        }
      );

      this.appService.startup();

  }

  ngAfterViewInit() {
    this.cdr.detectChanges();
  }



}
