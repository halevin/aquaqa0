import { NgbDateStruct, NgbTimeStruct } from '@ng-bootstrap/ng-bootstrap';

export class SearchOptions{

    // Correlator
    bl: boolean = true;
    aca : boolean = true;
    // Project type
    science : boolean = true;
    calibrator : boolean = false;
    // QA0 Flag
    unset : boolean = true;
    pending : boolean = false;
    pass : boolean = false;
    semipass : boolean = false;
    fail : boolean = false;

    dateStart: Date = new Date(2011,0,1,1,1,1);
    dateEnd: Date = new Date();

    projectCode : string;
    obsUnitSetStatusID : string;
    schedBlockName : string;
    schedBlockUID : string;
    execBlockUID : string;


    constructor(){}
}