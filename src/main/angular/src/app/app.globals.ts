import { Injectable } from '@angular/core';


@Injectable()
export class Globals {

    account = undefined;                         // Set by the startup procedure

    emptyList=[];


    loginURL = "/login-check";
    logoutURL = "/do-logout";

    helpURL = "";
    helpdeskEmail = "";
    currentARC = "";
    obsUnitSetManualList = [];
    obsUnitSetPipelineList = [];

    selectedClass = "bold";

    api = "";

    pendingRequests = 0;

    developmentMode = false;

    restServerURL : string;

    // Data

    assignResponse:string;
    manualResponse:string;
    pipelineResponse:string;


    operationType = {
      findAccounts : 'findAccounts',
      assign : 'assign',
      manual : 'manual',
      pipeline : 'pipeline',
      pipelineAssign : 'pipelineAssign',
      settings : 'settings'
    }

    currentOperationType = {};

    isDataLoaded = false;

    constructor() {

      if ( this.developmentMode ) {
        this.restServerURL = 'http://ga018573.ads.eso.org:10000/template/restapi';
      } else {
        this.restServerURL = '/template/restapi'; // production version
      }

    }


  }

