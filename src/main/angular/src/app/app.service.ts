import {Injectable, EventEmitter, Output} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/share';
import 'rxjs/add/operator/startWith';
import {Globals} from './app.globals';
import 'rxjs/add/observable/throw';
import {Utils} from "./app.utils";


@Injectable()
export class AppService {

    public data;

  change: EventEmitter<number> = new EventEmitter();
  loadingOn: EventEmitter<number> = new EventEmitter();
  loadingOff: EventEmitter<number> = new EventEmitter();
  @Output() assignResponse: EventEmitter<number> = new EventEmitter();

	private params = new URLSearchParams();

  httpPOSTOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/x-www-form-urlencoded',
      'Accept' : 'text/plain, text/html, application/xhtml+xml, application/xml, application/json',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Headers': '*'
    }),
    withCredentials: true
  };

  constructor(private http: HttpClient, private globals: Globals, private utils: Utils){
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

  getAccounts(search : string): void {
    this.getDataAsync(this.globals.restServerURL,'account', search, this.globals.emptyList, this.globals.operationType.findAccounts)
  }

  getRoles( user ) : any {
    let roles = [];
    if ( user != undefined && user.authorities != undefined) {
      user.authorities.forEach(function (auth) {
        roles.push(" "+auth.authority);
      });
    }
    return roles;
  }

  getDataAsync(server, api, urlParameter, parameters, operationType ){
    this.globals.currentOperationType = operationType;
    this.globals.api = api;
    var url = server + "/" + api + ((urlParameter.length != 0) ? ("/" + urlParameter) : "");
    console.log("Parameters "+JSON.stringify(parameters));
    if (parameters !== undefined) {
        url += "?";
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
      this.afterOperation(err.error.text, operationType);
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


  assign(mousid : string): void {
    this.postData(this.globals.restServerURL,'assign/'+this.utils.normalizeUID(mousid), this.globals.emptyList, this.globals.operationType.assign)
  }

  requestMOUSs(nMOUS : number, checkData: boolean, isManual : boolean): void {
    var params = [
      {key:"checkData", value:checkData},
      {key:"jsonOutput", value:"true"},
      {key:"isManual", value:isManual}
    ];

    let api = isManual?'manual':'pipelinelist';
    let operationType = isManual?this.globals.operationType.manual:this.globals.operationType.pipeline;
    this.getDataAsync(this.globals.restServerURL,api, nMOUS, params, operationType);
  }

  pipelineAssign(nMOUS : number, checkData: boolean, dryRun : boolean): void {
    var listMOUSs = [];
    this.globals.obsUnitSetPipelineList.forEach(function (par) {
      if (par.selected) {
        listMOUSs.push(par.ousStatusEntityId);
      }
    });
    const myheader = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    let params = new HttpParams();
    params = params.set('mode', dryRun?"try":"run");
    params = params.set('checkData', checkData.toString());
    params = params.set('pipe', nMOUS.toString());
    params = params.set('ousids', JSON.stringify(listMOUSs));
//    let params = {'mode' : dryRun?"try":"run", 'checkData' : checkData, 'pipe' : nMOUS, 'ousids' : listMOUSs};
    this.postData(this.globals.restServerURL,'pipeline', params, this.globals.operationType.pipelineAssign)
  }


  private extractDate(res: any) {
//    this.globals.accounts = res;
  }

  private extractSettings(res: any) {
    this.globals.helpURL = res.helpURL;
    this.globals.helpdeskEmail = res.email;
    this.globals.currentARC = (res.arc!=null)?res.arc.toUpperCase():"";
  }

  private extractAssignResponse(res: any) {
    this.globals.assignResponse = res;
  }

  private extractManualResponse(res: any) {
    this.globals.obsUnitSetManualList = res;
  }

  private extractPipelineResponse(res: any) {
    this.globals.obsUnitSetPipelineList = res;
    this.globals.obsUnitSetPipelineList.forEach(function (par) {
      par.selected = true;
    });
  }

  private extractPipelineAssignResponse(res: any) {
    this.globals.obsUnitSetPipelineList = [];
    this.globals.pipelineResponse = res;
  }


  private afterOperation(res: any, operationType:string) {
    if ( operationType == this.globals.operationType.assign){
      this.extractAssignResponse(res);
    } else if ( operationType==this.globals.operationType.manual){
      this.extractManualResponse(res);
    } else if (operationType==this.globals.operationType.pipeline) {
      this.extractPipelineResponse(res);
    } else if (operationType==this.globals.operationType.pipelineAssign) {
      this.extractPipelineAssignResponse(res);
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
    return Observable.throw(errMsg);
  }

}

export class TableSortCriteria {
  sortColumn: string;
  sortDirection: string;
}



