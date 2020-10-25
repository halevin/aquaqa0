import {throwError as observableThrowError} from 'rxjs';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {Globals} from './app.globals';

import {Utils} from "./app.utils";
import { SearchOptions } from './searchoptions';
import { ExecBlock } from './domain/execblock';
import { Emitters } from './app.emitters';
import { Processors } from './app.processors';
import { ExecBlockComment } from './domain/comment';


@Injectable()
export class AppService {

    public data;

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

  constructor(private http: HttpClient, private globals: Globals, private utils: Utils, 
    private sanitizer: DomSanitizer, private emitters : Emitters, private processors: Processors){
    this.params.set('format', 'json');
//    this.params.set('callback', 'JSONP_CALLBACK');
	}

  init() {
    console.log(">>> INIT");

    let parameters = [];
    // read settings
    this.getDataAsync(this.globals.restServerURL,'settings', "", parameters, this.globals.operationType.settings)

    this.setInitialSearchOptions();
    console.log("Setting initial search options")

    window.sessionStorage.clear();
  }

  setInitialSearchOptions(){
    this.globals.searchOptions = new SearchOptions();
    this.globals.timeInterval = this.globals.timeIntervals[0];
    this.globals.dateStart = {year:this.globals.searchOptions.dateStart.getUTCFullYear(), month:this.globals.searchOptions.dateStart.getUTCMonth()+1, day: this.globals.searchOptions.dateStart.getUTCDate()};
    this.globals.dateEnd = {year:this.globals.searchOptions.dateEnd.getUTCFullYear(), month:this.globals.searchOptions.dateEnd.getUTCMonth()+1, day: this.globals.searchOptions.dateEnd.getUTCDate()};
    this.globals.timeStart = {hour: this.globals.searchOptions.dateStart.getUTCHours(), minute: this.globals.searchOptions.dateStart.getUTCMinutes(), second: this.globals.searchOptions.dateStart.getUTCSeconds()};
    this.globals.timeEnd = {hour: this.globals.searchOptions.dateEnd.getUTCHours(), minute: this.globals.searchOptions.dateEnd.getUTCMinutes(), second: this.globals.searchOptions.dateEnd.getUTCSeconds()};
  }

  

  startup() {

    window.sessionStorage.clear();
    console.log("<<<<<<<<<<<<<<< location "+location.href);
    this.emitters.getLoadingOnEmitter().emit();

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

  getExecBlock(execBlockUID : string): void {
    if ( execBlockUID != null ) {
      let excBlockUIDNormalized = this.utils.replaceAll(this.utils.replaceAll(execBlockUID, "://","___"),"/","_");
      this.getDataAsync(this.globals.restServerURL,'execblock', excBlockUIDNormalized, this.globals.emptyList, this.globals.operationType.execBlock)
      this.getComments(execBlockUID);
    }
  }

  getAosCheck(execBlockUID : string): void {
    if ( execBlockUID != null ) {
      let excBlockUIDNormalized = this.utils.replaceAll(this.utils.replaceAll(execBlockUID, "://","___"),"/","_");
      this.getDataAsync(this.globals.restServerURL,'aoscheck', excBlockUIDNormalized, this.globals.emptyList, this.globals.operationType.aoscheck)
    }
  }

  getExecutionFraction(execBlockUID : string): void {
    if ( execBlockUID != null ) {
      let excBlockUIDNormalized = this.utils.replaceAll(this.utils.replaceAll(execBlockUID, "://","___"),"/","_");
      this.getDataAsync(this.globals.restServerURL,'execfraction', excBlockUIDNormalized, this.globals.emptyList, this.globals.operationType.executionFraction)
    }
  }

  getQA0Report(execBlockUID : string, format: string): void {
    if ( execBlockUID != null ) {
      let excBlockUIDNormalized = this.utils.replaceAll(this.utils.replaceAll(execBlockUID, "://","___"),"/","_");
      var parameters = [{key:"format", value:format}];
      this.getDataAsync(this.globals.restServerURL,'report', excBlockUIDNormalized, parameters, this.globals.operationType.executionFraction)
    }
  }

  getQA0StatusHistory(execBlockUID : string): void {
    if ( execBlockUID != null ) {
      let excBlockUIDNormalized = this.utils.replaceAll(this.utils.replaceAll(execBlockUID, "://","___"),"/","_");
      this.getDataAsync(this.globals.restServerURL,'qa0history', excBlockUIDNormalized, this.globals.emptyList, this.globals.operationType.qa0history)
    }
  }

  getComments(execBlockUID : string): void {
    if ( execBlockUID != null ) {
      let excBlockUIDNormalized = this.utils.replaceAll(this.utils.replaceAll(execBlockUID, "://","___"),"/","_");
      this.getDataAsync(this.globals.restServerURL,'comment', excBlockUIDNormalized, this.globals.emptyList, this.globals.operationType.comments)
    }
  }

  getAntennaFlags(execBlockUID : string): void {
    if ( execBlockUID != null ) {
      let excBlockUIDNormalized = this.utils.replaceAll(this.utils.replaceAll(execBlockUID, "://","___"),"/","_");
      this.getDataAsync(this.globals.restServerURL,'antenna-flags', excBlockUIDNormalized, this.globals.emptyList, this.globals.operationType.antennaFlags)
    }
  }

  getCoverages(execBlockUID : string): void {
    if ( execBlockUID != null ) {
      let excBlockUIDNormalized = this.utils.replaceAll(this.utils.replaceAll(execBlockUID, "://","___"),"/","_");
      this.getDataAsync(this.globals.restServerURL,'coverages', excBlockUIDNormalized, this.globals.emptyList, this.globals.operationType.coverages)
    }
  }

  getAtmosphere(execBlockUID : string): void {
    if ( execBlockUID != null ) {
      let excBlockUIDNormalized = this.utils.replaceAll(this.utils.replaceAll(execBlockUID, "://","___"),"/","_");
      this.getDataAsync(this.globals.restServerURL,'atmosphere', excBlockUIDNormalized, this.globals.emptyList, this.globals.operationType.atmosphere)
    }
  }

  updateExecBlock( execBlock : ExecBlock ) {
    let parameters = execBlock;
    this.postData(this.globals.restServerURL,'execblock', parameters, this.globals.operationType.updateExecBlock);
  }

  updateComment( comment : ExecBlockComment ) {
    let parameters = comment;
    this.postData(this.globals.restServerURL,'comment', parameters, this.globals.operationType.updateComment);
  }

  deleteComment( comment : ExecBlockComment ) {
    let parameters = comment.id;
    this.deleteData(this.globals.restServerURL,'comment', parameters, this.globals.operationType.deleteComment);
  }

  getDataAsync(server, api, urlParameter, parameters, operationType ){
    this.globals.currentOperationType = operationType;
    this.globals.api = api;
    var url = server + "/" + api + ((urlParameter.length != 0) ? ("/" + urlParameter) : "");
    console.log("Parameters "+JSON.stringify(parameters));
    if (parameters !== undefined) {
        if (urlParameter.length >= 0) { url += "?"; }
        let first = true;
        parameters.forEach(function (par) {
            url += ((first)?"":"&") + par.key + "=" + par.value;
            first = false;
        });
    }
    console.log("URL "+url);

    this.http.get(url).subscribe(response => {
      if (this.globals.pendingRequests>0) this.globals.pendingRequests--;
      this.processors.afterOperation(response, operationType);
    }, error => {this.handleError(error)});
}

  public postData(server, api, parameters, operationType){
    var url = server + "/" + api ;
    console.log("URL "+url);
    console.log("Parameters "+JSON.stringify(parameters));
    this.http.post(url, parameters, this.httpPOSTOptions).subscribe(response => {
      this.processors.afterOperation(response, operationType);
    }, err => {
      console.log("error " + JSON.stringify(err));
    });
  }

  public deleteData(server, api, parameters, operationType ){
    var url = server + "/" + api + "/" + parameters;
    console.log("URL "+url);
    console.log("Parameters "+JSON.stringify(parameters));
    this.http.delete(url, this.httpPOSTOptions).subscribe(response => {
      this.processors.afterOperation(response, operationType);
    }, error => {
      console.log("error " + JSON.stringify(error));
      this.processors.afterOperation(undefined, operationType);
    });
  }

  // Increase the value associated with a key by one
  private incMap<k>(m: Map<k, number>, key: k): void{
    m.set(key, m.has(key) ? m.get(key) + 1 : 1);
  }

  checkPendingProcesses(){
    if (this.globals.pendingRequests==0){
      console.log('pending requests '+this.globals.pendingRequests);
      this.emitters.getLoadingOffEmitter().emit();
      this.emitters.getChangeEmitter().emit();
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
    return observableThrowError(errMsg);
  }

  sanitizeTrusted(url: string): SafeUrl {
      return this.sanitizer.bypassSecurityTrustUrl(url);
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



