import {Injectable} from "@angular/core";
import {QA2DashboardEntry} from "./qa2dashboardentry";
import {QA2SearchOptions} from "./qa2dsearch";
import {QA2DashboardFilter} from "./qa2dfilter";
import {QA2DbColumns} from "./qa2dcolumns";
import {ListEntity} from "./listentity";
import {QA2ColumnSettings} from "./qa2columnsettings";
import {PropCount} from "./propcount";
import {TimeStatistic} from "./timestatistic";
import {TableName} from "./tablename";
import {Counter} from "./counter";
import {TimestatStatus} from "./timestatstatus";
import {HasDate} from "./hasdate";

@Injectable()
export class Qa2DashboardGlobals {
  // what to display for a date difference when it is 0 or null
  readonly DATE_NO_DIFF = '-';

  // what the backend gives for empty date fields
  readonly NO_DATE = '-';

  progressDisplayed : boolean = false;
  dashboardMode : boolean = false;

  filterVersion = 1;

  qa2DashboardEntries : QA2DashboardEntry[];
  qa2SearchOptions = new QA2SearchOptions();

  defaultColumns : Map<QA2DbColumns, QA2ColumnSettings> = new Map([
    [QA2DbColumns.state, new QA2ColumnSettings(true, 1, 'State', 'mousState')],
    [QA2DbColumns.substate, new QA2ColumnSettings(true, 2,'Substate', 'mousSubState')],
    [QA2DbColumns.drname, new QA2ColumnSettings(true, 3, 'DR Name', 'dataReducerName')],
    [QA2DbColumns.drarc, new QA2ColumnSettings(true, 4, 'DR ARC', 'dataReducerArc')],
    [QA2DbColumns.drnode, new QA2ColumnSettings(true, 5, 'DR Node', 'dataReducerNode')],
    [QA2DbColumns.plProcessingExec, new QA2ColumnSettings(true, 6, 'PL Proc. Exec', 'plProcessingExec')],
    [QA2DbColumns.projectCode, new QA2ColumnSettings(true, 7, 'Project code', 'projectsCode')],
    [QA2DbColumns.mousId, new QA2ColumnSettings(true, 8, 'MOUS Status ID', 'mousStatusId')],
    [QA2DbColumns.aquaLink, new QA2ColumnSettings(true, 9, 'AQUA link', 'mousStatusId')],
    [QA2DbColumns.tickets, new QA2ColumnSettings(true, 10, 'PRTSPR Tickets', 'prtsprTickets')],
    [QA2DbColumns.sbName, new QA2ColumnSettings(true, 11, 'SB Name', 'schedBlockName')],
    [QA2DbColumns.band, new QA2ColumnSettings(true, 12, 'Band', 'receiverBand')],
    [QA2DbColumns.qa0PassEBs, new QA2ColumnSettings(true, 13, 'QA0 Pass EBs', 'numberQA0PassExecBlocks')],
    [QA2DbColumns.drmname, new QA2ColumnSettings(true, 14, 'DRM', 'drmName')],
    [QA2DbColumns.preferredArc, new QA2ColumnSettings(true, 15, 'Preferred ARC','preferredArc')],
    [QA2DbColumns.contactScientist, new QA2ColumnSettings(true, 16, 'CS', 'contactScientistName')],
    [QA2DbColumns.lastQa0PassEB, new QA2ColumnSettings(true, 17, 'Last QA0 Pass EB', 'latestQa0PassExecBlockDate')],
    [QA2DbColumns.readyForProcessingDate, new QA2ColumnSettings(true, 18, 'ReadyForProcessing', 'firstDateReadyForProcessing')],
    [QA2DbColumns.processingDate, new QA2ColumnSettings(true, 19, 'Processing', 'firstDateProcessing')],
    [QA2DbColumns.readyForReviewDate, new QA2ColumnSettings(true, 20, 'ReadyForReview', 'lastDateReadyForReview')],
    [QA2DbColumns.verified, new QA2ColumnSettings(true, 21, 'Verified', 'lastDateVerified')],
    [QA2DbColumns.qa2Status, new QA2ColumnSettings(true, 22, 'QA2 Status', 'qa2Status')],
    [QA2DbColumns.deliveredDate, new QA2ColumnSettings(true, 23,'Delivered', 'lastDateDelivered')],
    [QA2DbColumns.staging, new QA2ColumnSettings(true, 24, 'Staging', 'stagingTimeDays')],
    [QA2DbColumns.assignment, new QA2ColumnSettings(true, 25, 'Assignment', 'assignmentTimeDays')],
    [QA2DbColumns.analysis, new QA2ColumnSettings(true, 26, 'Analysis', 'analysisTimeDate')],
    [QA2DbColumns.review, new QA2ColumnSettings(true, 27, 'Review', 'reviewTimeDate')],
    [QA2DbColumns.delivery, new QA2ColumnSettings(true, 28, 'Delivery', 'deliveryTimeDate')],
    [QA2DbColumns.obsToDelivery, new QA2ColumnSettings(true, 29, 'Obs. to Delivery', 'obsToDeliveryTimeDate')],
    [QA2DbColumns.pipecal, new QA2ColumnSettings(true, 30, 'Pipecal', 'pipeCalDate')],
    [QA2DbColumns.manualcal, new QA2ColumnSettings(true, 31, 'Manualcal', 'manualCalDate')],
    [QA2DbColumns.pipeimg, new QA2ColumnSettings(true, 32, 'Pipeimg', 'pipeImgDate')],
    [QA2DbColumns.manualimg, new QA2ColumnSettings(true, 33, 'Manualimg', 'manualImgDate')],
    [QA2DbColumns.comment, new QA2ColumnSettings(true, 34, 'Comment', 'comment')]
  ]);

  columns = this.defaultColumns;


    states = [];
  substates = [];
  cycles = [];

  filters : QA2DashboardFilter[] = [];
  selectedFilter : QA2DashboardFilter;
  newFilterName ; string;

  protrackOusURL = "";
  aquaOusURL = "";
  jiraURL = "";

  // get all values of enum, see https://github.com/Microsoft/TypeScript/issues/17198
  private enumValues(e: object): number[] {
    return Object.keys(e)
      .filter(k => typeof e[k as any] === "number")
      .map(k => e[k as any] as any);
  }

  readonly TABLE_NAMES : TableName[] = this.enumValues(TableName);
  readonly COUNTERS : Counter[] = this.enumValues(Counter);
  readonly TIMESTATS : TimestatStatus[] = this.enumValues(TimestatStatus);
  readonly HAS_DATES : HasDate[] = this.enumValues(HasDate);

  propCounts : Map<Counter, PropCount<string>[]> = new Map(this.COUNTERS.map(counter => [counter, []]));

  // timeStatistics : Map<TimestatStatus, TimeStatistic> = new Map(this.TIMESTATS.map(timeStatistic =>
  //   ([timeStatistic, new TimeStatistic(0, 0, 0)]))
  // );
  timeStatistics : TimeStatistic[] = [];

  dateNums : PropCount<string>[] = [];

  readonly arcs = [
    'NA',
    'EU',
    'EA'
  ];
  readonly execs = [
    'JAO',
    'NA',
    'EU',
    'EA'
  ];
  readonly projectGrades = [
    'A',
    'B',
    'C'
  ];
  readonly arrays = [
    '12M',
    '7M',
    'TP'
  ];

  readonly projectTypes = [
    new ListEntity("T", "Target of opportunity"),
    new ListEntity("L", "Large"),
    new ListEntity("S", "Standard"),
    new ListEntity("CAL", "Calibration"),
    new ListEntity("CSV", "Comissioning"),
    new ListEntity("D", "Directors discretionary time"),
    new ListEntity("ENG", "Engineering"),
    new ListEntity("MAI", "Maintenance"),
    new ListEntity("V", "VLBI"),
    new ListEntity("SIM", "Simulated data set")
  ];


  constructor() {
    // this.columns.set(QA2DbColumns.state, new QA2ColumnSettings(true, 1, 'State'));
    // this.columns.set(QA2DbColumns.substate, new QA2ColumnSettings(true, 2,'Substate'));
    // this.columns.set(QA2DbColumns.drname, new QA2ColumnSettings(true, 3, 'DR Name'));
    // this.columns.set(QA2DbColumns.drarc, new QA2ColumnSettings(true, 4, 'DR ARC'));
    // this.columns.set(QA2DbColumns.drnode, new QA2ColumnSettings(true, 5, 'DR Node'));
    // this.columns.set(QA2DbColumns.plProcessingExec, new QA2ColumnSettings(true, 6, 'PL Proc. Exec'));
    // this.columns.set(QA2DbColumns.projectCode, new QA2ColumnSettings(true, 7, 'Project code'));
    // this.columns.set(QA2DbColumns.mousId, new QA2ColumnSettings(true, 8, 'MOUS Status ID'));
    // this.columns.set(QA2DbColumns.aquaLink, new QA2ColumnSettings(true, 9, 'AQUA link'));
    // this.columns.set(QA2DbColumns.tickets, new QA2ColumnSettings(true, 10, 'PRTSPR Tickets'));
    // this.columns.set(QA2DbColumns.sbName, new QA2ColumnSettings(true, 11, 'SB Name'));
    // this.columns.set(QA2DbColumns.band, new QA2ColumnSettings(true, 12, 'Band'));
    // this.columns.set(QA2DbColumns.qa0PassEBs, new QA2ColumnSettings(true, 13, 'QA0 Pass EBs'));
    // this.columns.set(QA2DbColumns.drmname, new QA2ColumnSettings(true, 14, 'DRM'));
    // this.columns.set(QA2DbColumns.preferredArc, new QA2ColumnSettings(true, 15, 'Preferred ARC'));
    // this.columns.set(QA2DbColumns.contactScientist, new QA2ColumnSettings(true, 16, 'CS'));
    // this.columns.set(QA2DbColumns.lastQa0PassEB, new QA2ColumnSettings(true, 17, 'Last QA0 Pass EB'));
    // this.columns.set(QA2DbColumns.readyForProcessingDate, new QA2ColumnSettings(true, 18, 'ReadyForProcessing'));
    // this.columns.set(QA2DbColumns.processingDate, new QA2ColumnSettings(true, 19, 'Processing'));
    // this.columns.set(QA2DbColumns.readyForReviewDate, new QA2ColumnSettings(true, 20, 'ReadyForReview'));
    // this.columns.set(QA2DbColumns.verified, new QA2ColumnSettings(true, 21, 'Verified'));
    // this.columns.set(QA2DbColumns.qa2Status, new QA2ColumnSettings(true, 22, 'QA2 Status'));
    // this.columns.set(QA2DbColumns.deliveredDate, new QA2ColumnSettings(true, 23,'Delivered'));
    // this.columns.set(QA2DbColumns.staging, new QA2ColumnSettings(true, 24, 'Staging'));
    // this.columns.set(QA2DbColumns.assignment, new QA2ColumnSettings(true, 25, 'Assignment'));
    // this.columns.set(QA2DbColumns.analysis, new QA2ColumnSettings(true, 26, 'Analysis'));
    // this.columns.set(QA2DbColumns.review, new QA2ColumnSettings(true, 27, 'Review'));
    // this.columns.set(QA2DbColumns.delivery, new QA2ColumnSettings(true, 28, 'Delivery'));
    // this.columns.set(QA2DbColumns.obsToDelivery, new QA2ColumnSettings(true, 29, 'Obs. to Delivery'));
    // this.columns.set(QA2DbColumns.pipecal, new QA2ColumnSettings(true, 30, 'Pipecal'));
    // this.columns.set(QA2DbColumns.manualcal, new QA2ColumnSettings(true, 31, 'Manualcal'));
    // this.columns.set(QA2DbColumns.pipeimg, new QA2ColumnSettings(true, 32, 'Pipeimg'));
    // this.columns.set(QA2DbColumns.manualimg, new QA2ColumnSettings(true, 33, 'Manualimg'));
    // this.columns.set(QA2DbColumns.comment, new QA2ColumnSettings(true, 34, 'Comment'));
  }


}
