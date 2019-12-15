import { Injectable } from '@angular/core';
import {DataReducer} from "./datareducer";

export interface IOption {
  id: string,
  name: string
}

export const colors: any = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3'
  },
  blue: {
    primary: '#1e90ff',
    secondary: '#D1E8FF'
  },
  green: {
    primary: '#008000',
    secondary: '#3CB371'
  },
  gray: {
    primary: '#808080',
    secondary: '#C0C0C0'
  },
  yellow: {
    primary: '#FFFF00',
    secondary: '#FDF1BA'
  }
};



export const ARCs : any = [
  {id: 'NA', name: 'NA'},
  {id: 'EA', name: 'EA'},
  {id: 'EU', name: 'EU'},
  {id: 'JAO', name: 'JAO'}
];
const NodesNA : IOption[]  = [
  {id: 'ch', name: 'Charlottesville'},
  {id: 'ca', name: 'Canada'}
];
const NodesEA : IOption[] = [
  {id: 'ja', name: 'Japan'},
  {id: 'ko', name: 'Korea'},
  {id: 'ta', name: 'Taiwan'}
];
const NodesEU : IOption[] = [
  {id: 'ge', name: 'Germany'},
  {id: 'sw', name: 'Sweden'},
  {id: 'cz', name: 'Czech Republic'},
  {id: 'fr', name: 'France'},
  {id: 'uk', name: 'UK'},
  {id: 'ne', name: 'Netherlands'},
  {id: 'it', name: 'Italy'},
  {id: 'po', name: 'Portugal'},
  {id: 'eso', name: 'ESO'}
];
const NodesJAO : IOption[] = [
];

export const ARCNodes : Map<String, IOption[]> = new Map<String, IOption[]>();


@Injectable()
export class Globals {

    account = undefined;                         // Set by the startup procedure

    emptyList=[];
    accounts=[];

    searchVal : string;


    loginURL = "/login-check";
    logoutURL = "/do-logout";

    helpURL = "";
    helpdeskEmail = "";

    selectedClass = "bold";

    api = "";

    pendingRequests = 0;

    developmentMode = false;

    restServerURL : string;

    // Data
    statistics = {};
    coverages = [];

    currentExecBlockUID : string;


    operationType = {
      settings : 'settings',
      coverages : 'coverages'
    }

    drIdToDelete : String;

    currentOperationType = {};

    isDataLoaded = false;

    //QA2 Dashboard block

    constructor() {

      ARCNodes.set('NA', NodesNA);
      ARCNodes.set('EU', NodesEU);
      ARCNodes.set('EA', NodesEA);
      ARCNodes.set('JAO', NodesJAO);

      if ( this.developmentMode ) {
        this.restServerURL = 'http://ga018573.ads.eso.org:10000/aqua-qa0/restapi';
      } else {
        this.restServerURL = '/aqua-qa0/restapi'; // production version
      }

    }


  }

