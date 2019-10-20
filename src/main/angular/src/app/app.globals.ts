import { Injectable } from '@angular/core';

@Injectable()
export class Globals {
    developmentMode = false;


    account = undefined;                         // Set by the startup procedure
    route = undefined;                           // Set by the router defined in the init phase
    action = undefined;
    schedBlockId = undefined;
    execBlockId = undefined;
    projectId = undefined;
    projectElements = undefined;

    obopsConfig = "aquaConfig.properties"

//    restServerURL = "";
    ptServerURL = "";
    aquaServerURL = "";

    emptyList=[];


    loginURL = "/login-check";
    logoutURL = "/do-logout";
    //queryParams = parseQueryString();
    ptRestServerPath = "/rest";
    aquaRestServerPath = "/rest";
    ptReportServlet = "/ShiftlogReportCreatorServlet?reportType=PI&reportFormat=HTML&pc=";
    aquaQA0ReportURL = "/AquaReportCreatorServlet?reportType=EXECBLOCK_QA0&reportFormat=PDF&execblockuid=";
    aquaQA2ReportURL = "/AquaReportCreatorServlet?reportFormat=HTML&reportType=OUS_QA2";

    projectClassName = "ObsProject";
    proposalClassName = "ObsProposal";
    ousClassName = "ObsUnitSet";
    sbClassName = "SchedBlock";

    previousCycle = ["2012.1","2012.A"];
    currentCycle = ["2013.1", "2013.A"];

    searchString = "";
    projectFilterString = "";
    lastPruid = null;

    user = "";

    showOnlyActiveProjects = false;
    oncehided = false;

    nodeinstitutes = [];

    states = {
    //ObsProject
    approved : "Approved",
    inProgress : "InProgress",
    //reviewed : "Reviewed",
    completed : "Completed",
    partiallyCompleted : "PartiallyCompleted",
    canceled : "Canceled",
    notObserved : "NotObserved",
    doNotObserve : "DoNotObserve",
    phase1Submitted : "Phase1Submitted",
    phase2Submitted : "Phase2Submitted",
    pipelineProcessing : "PipelineProcessing",
    fullyObserved : "FullyObserved",
    partiallyObserved : "PartiallyObserved",
    delivered : "Delivered",
    rejected : "Rejected",
    //Member OUS
    observingTimedOut : "ObservingTimedOut",
    readyForProcessing : "ReadyForProcessing",
    readyToDeliver : "ReadyToDeliver",
    pipelineError : "PipelineError",
    processed : "Processed",
    qA2InProgress : "QA2InProgress",
    qA3InProgress : "QA3InProgress",
    manualProcessing  : "ManualProcessing ",
    reprocessingRequired : "ReprocessingRequired",
    verified : "Verified",
    //new
    processing : "Processing",
    processingProblem : "ProcessingProblem",
    readyForReview : "ReadyForReview",
    reviewing : "Reviewing",
    deliveryInProgress : "DeliveryInProgress",


    // SB specific
    ready : "Ready",
    running : "Running",
    suspended : "Suspended",
    waiting : "Waiting",
    broken : "Broken",
    repaired : "Repaired",
    deleted : "Deleted",
    calibratorCheck : "CalibratorCheck"}


    activeStates = [this.states.phase1Submitted,
        this.states.approved, this.states.phase2Submitted, this.states.ready,
        this.states.inProgress, this.states.observingTimedOut, this.states.broken,
        this.states.repaired]


    publicStates = {
    fullyObserved : "All data taken",
    someDataTaken : "Some data taken",
    partlyObserved : "Partly observed",
    noDataTaken : "Ready, no data taken",
    notAvailable : "N/A",
    approved : "Approved",
    observingTimedOut : "Timed out",
    completed : "Completed, all data delivered",
    partiallyCompleted : "Partially completed",
    verified : "Verified, QA2 pass",
    delivered : "Delivered",
    qa3InProgress : "QA3 in Progress",
    canceled : "Canceled",
    notObserved : "Not observed",
    doNotObserve : "Will not be observed",
    processing : "Being processed",
    rejected : "Rejected",
    ready : "Ready",
    running : "Running",
    phase1Submitted : "Phase 1 Submitted",
    phase2Submitted : "Phase 2 Submitted",
    waitingForPi : "Waiting for PI",
    waitingForTrigger : "Waiting for Trigger",

    all : "All",
    active : "Active",
    nonActive : "Non-active",
    readyForProcessing : "Ready for Processing"}


    entityStates = [this.publicStates.all, this.publicStates.active, this.publicStates.nonActive];

    detailPages = {ObsProposal : 'proposal-details', SchedBlock : 'schedblock-details'};

    ticketCounts ={};  // The global tickets statistics object
    ticketCountsAll = 0;

    modes = {pi : "pi",coi : "coi", cs : "cs", delegee:"delegee"};

    depthOffset = 10;
    numNews = 3;
    numNewsCS = 3;
    newsStep = 3;
    start = new Date();

    currentProjectTree = {};

    pendingRequests = 0;

    mode = this.modes.pi;

    restServerURL : string;

    // Data
    statistics = {
      // 'activeprojects':0,
      // 'projects':0,
      // 'activeprojectsCoPI':0,
      // 'projectsCoPI':0,
      // 'activeprojectsCoI':0,
      // 'projectsCoI':0,
      // 'activeschedblocks':0,
      // 'schedblocks':0,
      // 'activeschedblocksCoPI':0,
      // 'schedblocksCoPI':0,
      // 'activeschedblocksCoI':0,
      // 'schedblocksCoI':0,
      // 'activeprojectsDelegee':0,
      // 'projectsDelegee':0,
      // 'activeschedblocksDelegee':0,
      // 'schedblocksDelegee':0
    };

    news = {};
    newsDate;
    projectList = [];
    filteredProjectList = [];
    sbList = [];
    filteredSbList = [];
    proposalDetails;
    sbDetails;
    sbHistory;
    ousHistory;
    ebDetails;
    prevSBName : string;


  dataType = {
        statistics:'statistics',
        news:'news',
        projectList:'projectList',
        sbList:'sbList',
        project:'project',
        proposal:'proposal',
        sb:'sb',
        eb:'eb',
        sbHistory:'sbHistory',
        ousHistory:'ousHistory',
        ptURL:'ptURL'
    };

    currentDataType = {};

    isDataLoaded = false;
    delegeeVisible = false;
    coPIVisible = false;
    fastMode = true;
    sidebarDisplayed = true;

    entityId = "";
    api = "";


    newsFormatted = "";



    nodes = {"institutes":[
        {"code":"21208", "node":"Dutch (Allegro)"},
        {"code":"21210", "node":"Dutch (Allegro)"},
        {"code":"21108", "node":"German"},
        {"code":"212", "node":"German"},
        {"code":"21109", "node":"German"},
        {"code":"21054", "node":"Czech"},
        {"code":"21055", "node":"Czech"},
        {"code":"21093", "node":"IRAM"},
        {"code":"21163", "node":"Italian"},
        {"code":"21256", "node":"Nordic"},
        {"code":"21257", "node":"Nordic"},
        {"code":"21219", "node":"Portuguese (PACE)"},
        {"code":"21224", "node":"Portuguese (PACE)"},
        {"code":"21225", "node":"Portuguese (PACE)"},
        {"code":"21306", "node":"UK"},
        {"code":"22086", "node":"North American"},
        {"code":"22091", "node":"Canadian"},
        {"code":"22071", "node":"NAOJ"},
        {"code":"37944", "node":"NAOJ"},
        {"code":"21505", "node":"ASIAA"},
        {"code":"180", "node":"KASI"},
        {"code":"33970", "node":"KASI"},
        {"code":"33977", "node":"KASI"},
        {"code":"36288", "node":"KASI"},
        {"code":"35036", "node":"KASI"},
        {"code":"204", "node":"KASI"}


      ],
        "urls":
        [
          {"node":"Dutch (Allegro)", "url":"http://www.alma-allegro.nl"},
          {"node":"Czech", "url":"http://www.asu.cas.cz/alma"},
          {"node":"German", "url":"https://www.astro.uni-bonn.de/ARC/"},
          {"node":"IRAM", "url":"http://www.iram.fr/IRAMFR/ARC/"},
          {"node":"Italian", "url":"http://www.alma.inaf.it/index.php/Italian_ALMA_Regional_Centre"},
          {"node":"Nordic", "url":"http://www.nordic-alma.se"},
          {"node":"Portuguese (PACE)", "url":"http://pace.oal.ul.pt"},
          {"node":"UK", "url":"http://almadev.jb.man.ac.uk"},
          {"node":"North American", "url":"https://science.nrao.edu/facilities/alma/intro-naasc"},
          {"node":"Canadian", "url":"http://www.nrc-cnrc.gc.ca/eng/services/observing/alma.html"},
          {"node":"NAOJ", "url":"https://researchers.alma-telescope.jp/e/ea-arc/"},
          {"node":"ASIAA", "url":"http://alma.asiaa.sinica.edu.tw/"},
          {"node":"KASI", "url":"http://www.kasi.re.kr/english/Research/ALMA.aspx"}


        ]
      }

  constructor() {

    this.restServerURL = '/qa0/restapi'; // production version

  }


}

