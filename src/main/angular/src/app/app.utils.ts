import { Injectable, EventEmitter } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/share';
import 'rxjs/add/operator/startWith';
import {Globals} from './app.globals';
import 'rxjs/add/observable/throw';

@Injectable()
export class Utils {

  constructor(public globals: Globals) {
  }


  extractAPIFromURL(url:string):string{
    console.log("====== url "+url);
    if (url==undefined) return '';
    var substr = url.substr(1,url.length+1);
    console.log("====== substr "+substr);
    if (substr.indexOf('/')>=0){
      return substr.substr(0,substr.indexOf('/'));
    } else {
      return substr;
    }
  }

  normalizeUID(uid:string):string{
    console.log("normalizing UID "+uid);
    if (uid==undefined) return '';
    let result = this.replaceAll(uid.replace("://","___"),'/',"_");
    return result;
  }

  replaceAll(str, find, replace) {
    if (str!==undefined){
      return str.replace(new RegExp(this.escapeRegExp(find), 'g'), replace);
    } else {
      return str;
    }
  }

  escapeRegExp(str) {
    if (str!==undefined){
      return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
    } else {
      return str;
    }
  }


  dateToString(date){
    var month = 1+date.getMonth();
    var day = date.getDate();
    return date.getFullYear()+"-"+(month<10?'0'+month:month)+"-"+(day<10?'0'+day:day);
  }

  getDate(seconds){
    var date = new Date(null);
    date.setSeconds(seconds/1000);
    return this.dateToString(date);
  }

  checkRole(role : string) : boolean {
    console.log("Role : " + role);
    console.log("Account : " + JSON.stringify(this.globals.account));
    return this.globals.account != undefined && JSON.stringify(this.globals.account.authorities).indexOf(role)>0
  }

  isNullOrUndefined(value) : boolean {
    if (value === null || value === undefined || value.length == 0) {
      return true;
    }
    return false;
  }

  booleanToYesNo(value : boolean) {
    return (value)?"Yes":"No";
  }


  getDateFromTimestamp(timestamp : string) : string{
    return (new Date(timestamp)).toDateString();
  }

}



