import { Injectable, EventEmitter } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {catchError, map, share, startWith} from 'rxjs/operators';
import {BehaviorSubject} from 'rxjs';
import {Globals} from './app.globals';
import {Render} from './app.render';
import { CookieService } from 'ngx-cookie-service';
import {throwError} from 'rxjs';


@Injectable()
export class AppService {

  public data;

  change: EventEmitter<number> = new EventEmitter();
  expandSidebar: EventEmitter<number> = new EventEmitter();
  hideSidebar: EventEmitter<number> = new EventEmitter();
  loadingOn: EventEmitter<number> = new EventEmitter();
  loadingOff: EventEmitter<number> = new EventEmitter();

	private params = new URLSearchParams();

  constructor(private http: HttpClient, private globals: Globals,
              private  cookieService: CookieService, 
              private render:Render){
    this.params.set('format', 'json');
//    this.params.set('callback', 'JSONP_CALLBACK');
	}

  init() {
    console.log(">>> INIT");
    window.sessionStorage.clear();
    // Load the main application tag, it will then load all sub-tags

    this.getProtrackServerURL();
  }

  startup() {

    this.globals.user = location.href.split('user=')[1];
    window.sessionStorage.clear();
    console.log("<<<<<<<<<<<<<<< location "+location.href+" user "+this.globals.user);
    this.loadingOn.emit();

    var url = this.globals.restServerURL + "/account" + ((this.globals.user != undefined && this.globals.user.length != 0)?("?user="+this.globals.user):"");
    this.http.get(url).subscribe(response => {
      this.gotAccountInfo(response);
    });

  }

gotAccountInfo( response ) {

  var accountInfo = response;
  console.log("gotAccountInfo "+JSON.stringify(accountInfo));

  if( accountInfo === undefined || accountInfo.accountId == null ) {

      // We need to log into the REST server
      // Set the return-url cookie so that the server can take us back here
      // when startup is completed; then redirect to the startup page
      var url = window.location.href;
      var domain = this.extractDomain( this.globals.restServerURL+this.globals.loginURL );
      this.setCookie( 'return-url',                    // cookie name
          url,              // cookie value
          domain,                                 // cookie domain
          "/" );                                  // cookie path
      window.location.href = (this.globals.restServerURL+this.globals.loginURL);          // redirect to login URL
      throw( 'deliberate error' );                // break the request chain
  }

  // All is well, we are logged in, let's save account info
  this.globals.account = accountInfo;
  this.globals.isDataLoaded = true;
  this.init();
  this.initHomePageInfo();
}

extractDomain( url ) {
    var t = url.split('/')[2].split(':')[0].split('.');
    var n = t.length;
    var domain = t[n - 2] + "." + t[n - 1];
    return domain;
}


/**
 * Deletes and re-sets a cookie with the input parameters
 */
setCookie( name, value, domain, path ) {
  var expires = new Date(1970,0,1);
  var expiresNew = new Date(2019,0,1);
  this.cookieService.set(name,value,expires,path,domain);  // expire date is in the past: delete the cookie
  this.cookieService.set(name,value,expiresNew,path,domain);
}

  getChangeEmitter() {
    return this.change;
  }

  getExpandSideBarEmitter() {
    return this.expandSidebar;
  }

  getHideSideBarEmitter() {
    return this.hideSidebar;
  }

  getLoadingOnEmitter() {
    return this.loadingOn;
  }

  getLoadingOffEmitter() {
    return this.loadingOff;
  }

  getProtrackServerURL(): void {
    this.getDataAsync(this.globals.restServerURL,'protrackurl', "", this.globals.emptyList, this.globals.dataType.ptURL)
  }

  getDataAsync(server, api, encodedUid, parameters, dataType ){
    this.globals.currentDataType = dataType;
    this.globals.api = api;
    var dataURI = api+encodedUid+JSON.stringify(parameters)+this.globals.mode;
    if (!window.sessionStorage[dataURI]) {
        var url = server + "/" + api + ((encodedUid.length != 0) ? ("/" + encodedUid) : "") + "?" +
            ((this.globals.mode == this.globals.modes.cs) ? ("csmode=true&inst=" + this.globals.nodeinstitutes + "&") : "") +
            ((this.globals.user != undefined && this.globals.user.length != 0) ? ("&user=" + this.globals.user + "&") : "");
        var fast = false;
        if (parameters !== undefined) {
            parameters.forEach(function (par) {
                url += par.key + "=" + par.value + "&";
                if (par.key == "fast"){fast = par.value;}
            });
        }

        if (fast || api!="project-list") {
          if ( this.globals.pendingRequests == 0 ) {
            this.loadingOn.emit();
          }
          this.globals.pendingRequests++;
        } else {
          this.globals.pendingRequests=0;
        }

      this.http.get(url).subscribe(response => {
          if (this.globals.pendingRequests>0) this.globals.pendingRequests--;
          window.sessionStorage[dataURI] = JSON.stringify(response);
          this.extractData(response, dataType);
        }, error => {this.handleError(error)});
      } else {
        if (this.globals.pendingRequests>0) this.globals.pendingRequests--;
         this.extractData(JSON.parse(window.sessionStorage[dataURI]),dataType);
    }
}

extractData(response, dataType){
	
}

  initHomePageInfo(){
  }

  checkPendingProcesses(){
    if (this.globals.pendingRequests==0){
      this.loadingOff.emit();
      this.change.emit();
    }
  }

  handleError (error: Response | any) {
    // In a real world app, you might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      errMsg = `${error.status} - ${error.statusText || ''}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    // console.log("error "+errMsg);
    // console.log("this.globals "+this.globals);
    if (this.globals!=undefined){
      this.globals.pendingRequests=0;
      this.checkPendingProcesses();
    }

  }

  extractAPIFromURL(url:string):string{
    if (url==undefined) return '';
    var substr = url.substr(1,url.length+1);
    if (substr.indexOf('/')>=0){
      return substr.substr(0,substr.indexOf('/'));
    } else {
      return substr;
    }
  }

  truncate( desc ) {
    var limit = 30;
    if( this.globals.action != undefined && desc.length > limit ) {
        var descFull = desc.slice(0);
        descFull = descFull.replace('"',"&quot;");
        descFull = descFull.replace("'","&#39;");
        var value = '<span title = "'+descFull+'">' + desc.substr( 0, limit ) + '...</span>';
        return value;
    }
    return desc;
  };


}

export class TableSortCriteria {
  sortColumn: string;
  sortDirection: string;
}

