
export class ExecBlock{

    obsProjectCode: string = "";
    schedBlockName : string = "";
    schedBlockUID : string = "";
    ousStatusUid : string = "";
    jiraTickets : string;
    // duration : string;
    startTime : string;
    endTime : string;
    qa0Status : string = "Unset";
    qa0StatusReason : string = "-";
    execBlockUid : string = ""; 
    execFraction : number = 0.0;
    representativeFrequency : number = 0.0;
    correlator : string = "";
    array : string = "";
    weatherData : string;
    phaseRMS : string;
    antennasInfo : string;
    aosCheckSummary : any;
    finalComment : string = "";

    executionFractionReport : string = "";
    isFluxCalibratorMissing : boolean = false;

    constructor(){}
}