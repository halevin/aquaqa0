import { Component, OnInit } from '@angular/core';
import { IOption, Globals } from '../app.globals';
import { AppService } from '../app.service';
import { Utils } from '../app.utils';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  

  constructor(private service : AppService, private globals : Globals, private utils : Utils) { 
  }

  ngOnInit() {
  }

  timeIntervalChange(){
    let currentDate = new Date();
    console.log(JSON.stringify(currentDate));
    
    let startDate : Date;
    if (this.globals.timeInterval == this.globals.timeIntervals[1]) {
      startDate = new Date(currentDate.getTime() - (1000 * 60 * 60 * 2));
    } else if (this.globals.timeInterval == this.globals.timeIntervals[2]) {
      startDate = new Date(currentDate.getTime() - (1000 * 60 * 60 * 8));
    } else if (this.globals.timeInterval == this.globals.timeIntervals[3]) {
      startDate = new Date(currentDate.getTime() - (1000 * 60 * 60 * 24));
    }

    if ( startDate != null ) {
      this.globals.dateStart = {year:startDate.getUTCFullYear(), month:startDate.getUTCMonth()+1, day: startDate.getUTCDate()};
      this.globals.dateEnd = {year:currentDate.getUTCFullYear(), month:currentDate.getUTCMonth()+1, day: currentDate.getUTCDate()};
      this.globals.timeStart = {hour: startDate.getUTCHours(), minute: startDate.getUTCMinutes(), second: startDate.getUTCSeconds()};
      this.globals.timeEnd = {hour: currentDate.getUTCHours(), minute: currentDate.getUTCMinutes(), second: currentDate.getUTCSeconds()};
    }
  }

  search(){
    this.globals.searchSpinnerVisible = true;

    this.globals.searchOptions.dateStart = this.utils.ngdDateTimeToDate(this.globals.dateStart, this.globals.timeStart); 
    this.globals.searchOptions.dateEnd = this.utils.ngdDateTimeToDate(this.globals.dateEnd, this.globals.timeEnd); 
    this.service.postData(this.globals.restServerURL, 'search', this.globals.searchOptions, this.globals.operationType.execBlocks);
  }


  reset(){
    this.service.setInitialSearchOptions();
  }

}
