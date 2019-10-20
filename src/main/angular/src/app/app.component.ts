import { Component, OnInit, ChangeDetectorRef  } from '@angular/core';
import {AppService} from './app.service';
import {Globals} from './app.globals';
import {NavigationSidebarComponent} from './navigation-sidebar/navigation-sidebar.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  sidebarDisplayed = true;
  progressDisplayed = false;
  class = "active";
  subscriptionOn: any;
  subscriptionOff: any;
  subscriptionHideSideBar: any;
  subscriptionExpandSideBar: any;

  constructor(private cdr: ChangeDetectorRef, private appService: AppService, private globals: Globals, private route: ActivatedRoute){
	}

	ngOnInit():void {

			this.subscriptionOn = this.appService.getLoadingOnEmitter()
			.subscribe(item =>{
          if (this.globals.fastMode || (this.globals.api !== "project-list" && this.globals.api !== "schedblock-list") ){
            Promise.resolve(null).then(() => this.progressDisplayed = true);
          }
        }
      );
			this.subscriptionOff = this.appService.getLoadingOffEmitter()
			.subscribe(item =>{
        Promise.resolve(null).then(() => this.progressDisplayed = false);
        }
      );
			this.subscriptionHideSideBar = this.appService.getHideSideBarEmitter()
			.subscribe(item =>{
          this.sidebarDisplayed = false;
          this.class = "";
        }
      );
			this.subscriptionExpandSideBar = this.appService.getExpandSideBarEmitter()
			.subscribe(item =>{
          this.sidebarDisplayed = true;
          this.class = "active";
        }
      );
      this.route.paramMap.subscribe(params => {
        this.globals.mode = params.get('mode');
      });

      this.appService.startup();

  }

  ngAfterViewInit() {
    this.cdr.detectChanges();
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
