import { Injectable, EventEmitter } from '@angular/core';
import {isNullOrUndefined} from "util";
import { Globals } from './app.globals';

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
    return true;
    // console.log("Role : " + role);
    // console.log("Account : " + JSON.stringify(this.globals.account));
    // return this.globals.account != undefined && JSON.stringify(this.globals.account.authorities).indexOf(role)>0
  }

  isNullOrUndefined(value) : boolean {
    if (value === null || value === undefined || value.length == 0 || /^\s*$/.test(value)) {
      return true;
    }
    return false;
  }

  hasQualification(dr : any , qual : string) {
    return (! isNullOrUndefined(dr.qualifications) && dr.qualifications.includes(qual))?"Yes":"No";
  }

  booleanToYesNo(value : boolean) {
    return (value)?"Yes":"No";
  }


  getDateFromTimestamp(timestamp : string) : string{
    return (new Date(timestamp)).toDateString();
  }

  getColorString( avail : string) : string {

    if ( avail == 'UNSET') {
      return "#808080";
    } else if ( avail == 'AVAIL') {
      return "#008000";
    } else if ( avail == 'AWAY') {
      return "#ad2121";
    } else if ( avail == 'IFNEED') {
      return "#FFFF00";
    }

    return "#ad2121";
  }

  getColorClass( avail : string) : string {

    if ( avail == 'UNSET') {
      return "unset";
    } else if ( avail == 'AVAIL') {
      return "avail";
    } else if ( avail == 'AWAY') {
      return "away";
    } else if ( avail == 'IFNEED') {
      return "ifneed";
    }

    return "away";
  }


}



