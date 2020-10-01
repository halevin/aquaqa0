import { Injectable } from '@angular/core';
import { ExecBlock } from './domain/execblock';
import { NgbDateStruct, NgbTimeStruct } from '@ng-bootstrap/ng-bootstrap';
import { SearchOptions } from './searchoptions';
import { QA0Reason } from './domain/qareason';

export interface IOption {
  id: string,
  name: string
}

@Injectable()
export class Globals {

    account = undefined;                         // Set by the startup procedure

    emptyList=[];
    accounts=[];
    qa0Statuses = ['Pass','SemiPass','Unset','Pending','Fail'];
    qa0Reasons = [];

    searchVal : string;
    public execBlocks : ExecBlock[] = [];
    public execBlock : ExecBlock = new ExecBlock();


    loginURL = "/login-check";
    logoutURL = "/do-logout";

    webShiftLogURL = "";
    aquaQA2URL = "";
    protrackURL = "";

    helpURL = "";
    helpdeskEmail = "";

    selectedClass = "bold";

    api = "";

    pendingRequests = 0;

    developmentMode = false;

    restServerURL : string;

    // Data
    aosCheckSummary;
    statistics = {};
    coverages = [];
    atmosphere = {};
    qa0StatusHitory = [];
    antennaFlags = [];

    currentExecBlockUID : string;


    qa0SemiPassReasons: QA0Reason[] = [];
    qa0PendingReasons: QA0Reason[] = [];

    operationType = {
      execBlocks : 'execblocks',
      execBlock : 'execblock',
      aoscheck : 'aoscheck',
      executionFraction : 'executionFraction',
      qa0history : 'qa0history',
      antennaFlags : 'antennaFlags',
      settings : 'settings',
      coverages : 'coverages',
      atmosphere : 'atmosphere',
      updateExecBlock : 'updateExecBlock'
    }

    drIdToDelete : String;

    currentOperationType = {};

    isDataLoaded = false;
    execBlockSpinnerVisible = true;
    searchSpinnerVisible = false;

    dateStart: NgbDateStruct;
    dateEnd: NgbDateStruct;
    timeStart : NgbTimeStruct;
    timeEnd : NgbTimeStruct;
    timeInterval : IOption;
    public searchOptions : SearchOptions = new SearchOptions();

    public timeIntervals : IOption[] = [
      {id: 'other', name: 'Other'},
      {id: 'last2hours', name: 'Last 2 hours'},
      {id: 'last8hours', name: 'Last 8 hours'},
      {id: 'lastDay', name: 'Last day'},
    ];
  
  

    constructor() {

      if ( this.developmentMode ) {
        this.restServerURL = 'http://ga018573.ads.eso.org:10000/aqua-qa0/restapi';
      } else {
        this.restServerURL = '/aqua-qa0/restapi'; // production version
      }

    }


  }

