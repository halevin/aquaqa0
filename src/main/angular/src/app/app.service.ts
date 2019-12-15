import {throwError as observableThrowError} from 'rxjs';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {Globals} from './app.globals';

import {DataReducer} from "./datareducer";
import {Availability} from "./availability";
import {QA2DashboardEntry} from "./qa2dashboardentry";
import {Utils} from "./app.utils";
import {endOfDay, startOfDay} from 'date-fns';
import {Day} from "./day";
import {Qa2DashboardGlobals} from "./app.qa2dashboard.globals";
import {QA2DashboardFilter} from "./qa2dfilter";
import {PropCount} from "./propcount";
import {TimeStatistic} from "./timestatistic";
import {Counter} from "./counter";
import {TimestatStatus} from "./timestatstatus";
import {HasDate} from "./hasdate";


@Injectable()
export class AppService {

    public data;

  change: EventEmitter<number> = new EventEmitter();
  loadingOn: EventEmitter<number> = new EventEmitter();
  loadingOff: EventEmitter<number> = new EventEmitter();
  accountSelected: EventEmitter<number> = new EventEmitter();
  activatedAsDR: EventEmitter<number> = new EventEmitter();
  deletedFromDR: EventEmitter<number> = new EventEmitter();

	private params = new URLSearchParams();

  httpPOSTOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json',
      'Accept' : 'text/plain, text/html, application/xhtml+xml, application/xml, application/json',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Headers': '*'
    }),
    withCredentials: true
  };

  constructor(private http: HttpClient, private globals: Globals, private qa2dashboardGlobals : Qa2DashboardGlobals, private utils: Utils, private sanitizer: DomSanitizer){
    this.params.set('format', 'json');
//    this.params.set('callback', 'JSONP_CALLBACK');
	}

  init() {
    console.log(">>> INIT");

    let parameters = [];
    // read settings
    this.getDataAsync(this.globals.restServerURL,'settings', "", parameters, this.globals.operationType.settings)

    window.sessionStorage.clear();
  }

  startup() {

    window.sessionStorage.clear();
    console.log("<<<<<<<<<<<<<<< location "+location.href);
    this.loadingOn.emit();

    var url = this.globals.restServerURL + "/account";
    this.http.get(url).subscribe(response => this.gotAccountInfo(response));


  }

gotAccountInfo( response ) {

  var accountInfo = response;
  console.log("gotAccountInfo "+JSON.stringify(accountInfo));

  if( accountInfo === undefined || accountInfo.username == null ) {

      // We need to log into the REST server
      // Set the return-url cookie so that the server can take us back here
      // when startup is completed; then redirect to the startup page
      var url = window.location.href;
      var domain = this.extractDomain( this.globals.restServerURL+this.globals.loginURL );

      this.setCookie( 'return-url', encodeURIComponent(url), domain,'/' );
      if ( ! this.globals.developmentMode) {
        window.location.href = (this.globals.restServerURL+this.globals.loginURL);          // redirect to login URL
        throw( 'deliberate error' );                // break the request chain
      }
  }

  // All is well, we are logged in, let's save account info
  this.globals.account = accountInfo;
  this.globals.isDataLoaded = true;
  this.init();
}

  sleep(milliseconds) {
    var start = new Date().getTime();
    for (var i = 0; i < 1e7; i++) {
      if ((new Date().getTime() - start) > milliseconds){
        break;
      }
    }
  }

extractDomain( url ) {
  console.log("URL "+url);
    var t = url.split('/')[2].split(':')[0].split('.');
    var n = t.length;
    var domain = t[n - 2] + "." + t[n - 1];
    return domain;
}


/**
 * Deletes and re-sets a cookie with the input parameters
 */
setCookie( name: string, value:string, domain:string, path:string ) {
  this.set(name,value,-1,path,domain, false);
  this.set(name,value,10,path,domain, false);
}

  public set(name: string, value: string, expires?: number | Date, path?: string, domain?: string, secure?: boolean) {
    let cookieStr = name + '=' + value;
    console.log("+++++++++ storing cookie " + cookieStr);

    if (! this.utils.isNullOrUndefined(expires) ) {
      if (typeof expires === 'number') {
        let dtExpires = new Date(new Date().getTime() + expires * 1000 * 60 * 60 * 24);
        cookieStr += '; expires=' + dtExpires.toUTCString();
      } else {
        cookieStr += '; expires=' + expires.toUTCString();
      }
    }
    console.log("+++++++++ storing cookie " + cookieStr);



    if ( ! this.utils.isNullOrUndefined(path) ) {
      cookieStr += '; path=' + path ;
    }
    console.log("+++++++++ storing cookie " + cookieStr);
    if ( ! this.utils.isNullOrUndefined(domain) ) {
      cookieStr += '; domain=' + domain;
    }
    console.log("+++++++++ storing cookie " + cookieStr);
    if ( secure ) {
      cookieStr += '; secure ';
    }

    console.log("+++++++++ storing cookie " + cookieStr);
    document.cookie = cookieStr;
    console.log("+++++++++ cookie stored " + document.cookie);

  }


  getChangeEmitter() {
    return this.change;
  }

  getLoadingOnEmitter() {
    return this.loadingOn;
  }

  getLoadingOffEmitter() {
    return this.loadingOff;
  }

  getCoverages(execBlockUID : string): void {
    this.getDataAsync(this.globals.restServerURL,'coverages', execBlockUID, this.globals.emptyList, this.globals.operationType.coverages)
  }

  getDataAsync(server, api, urlParameter, parameters, operationType ){
    this.globals.currentOperationType = operationType;
    this.globals.api = api;
    var url = server + "/" + api + ((urlParameter.length != 0) ? ("/" + urlParameter) : "");
    console.log("Parameters "+JSON.stringify(parameters));
    if (parameters !== undefined) {
        if (urlParameter.length == 0) { url += "?"; }
        let first = true;
        parameters.forEach(function (par) {
            url += ((first)?"":"&") + par.key + "=" + par.value;
            first = false;
        });
    }
    console.log("URL "+url);

    this.http.get(url).subscribe(response => {
      if (this.globals.pendingRequests>0) this.globals.pendingRequests--;
      this.afterOperation(response, operationType);
    }, error => {this.handleError(error)});
}

  public postData(server, api, parameters, operationType ){
    var url = server + "/" + api ;
    console.log("URL "+url);
    console.log("Parameters "+JSON.stringify(parameters));
    this.http.post(url, parameters, this.httpPOSTOptions).subscribe(response => {
      this.afterOperation(response, operationType);
    }, err => {
      this.afterOperation(undefined, operationType);
      console.log("error " + JSON.stringify(err));
    });
  }

  public deleteData(server, api, parameters, operationType ){
    var url = server + "/" + api + "/" + parameters;
    console.log("URL "+url);
    console.log("Parameters "+JSON.stringify(parameters));
    this.http.delete(url, this.httpPOSTOptions).subscribe(response => {
      this.afterOperation(response, operationType);
    }, error => {
      console.log("error " + JSON.stringify(error));
      this.afterOperation(undefined, operationType);
    });
  }


	private extractCoverages(res: any) {
    this.globals.coverages = res;
  }

  private extractSettings(res: any) {
    this.globals.helpURL = res.helpURL;
    this.globals.helpdeskEmail = res.email;
    console.log("settings" + JSON.stringify(res));
  }

  // Increase the value associated with a key by one
  private incMap<k>(m: Map<k, number>, key: k): void{
    m.set(key, m.has(key) ? m.get(key) + 1 : 1);
  }

  private mapToPropCounts<k>(m: Map<k, number>): PropCount<k>[] {
    return Array.from(m, ([prop, count]) => new PropCount(prop, count));
  }


  private afterOperation(res: any, operationType:string) {
    if ( operationType == this.globals.operationType.coverages){
      this.extractCoverages(res);
    } else if (operationType==this.globals.operationType.settings) {
      this.extractSettings(res);
    }
  }

  checkPendingProcesses(){
    if (this.globals.pendingRequests==0){
      console.log('pending requests '+this.globals.pendingRequests);
      this.loadingOff.emit();
      this.change.emit();
    }
  }

  handleError (error: Response | any) {
    // In a real world app, you might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response) {
      const err = error;
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    // console.log("error "+errMsg);
    // console.log("this.globals "+this.globals);
    // if (this.globals!=undefined){
    // this.globals.pendingRequests=0;}
    return observableThrowError(errMsg);
  }

  sanitizeTrusted(url: string): SafeUrl {
      return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  truncate(length: number, str: string) {
      return str.length <= length ? str : str.substring(0, length - 2) + "...";
  }

  //Solution adapted from http://stackoverflow.com/questions/14267781/sorting-html-table-with-js
  sort_table(criteria: TableSortCriteria, list, isText) {
      let compval = array => {
          let col = array[criteria.sortColumn]
          return col != undefined && typeof col.toLowerCase === "function"
              ? col.toLowerCase()
              : col;
      }
      list.sort((a, b) => {
          let returnval =
              a == null               ?  1
            : b == null               ? -1
            : compval(a) < compval(b) ?  1
            : /* otherwise ? */         -1;
          return (criteria.sortDirection === 'desc')
              ?   returnval
              : - returnval;
      });
  }

}

export class TableSortCriteria {
  sortColumn: string;
  sortDirection: string;
}



