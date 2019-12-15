export class QA2DashboardEntry {
  mousState: string;
  mousSubState: string;
  dataReducerName: string;
  dataReducerArc: string;
  dataReducerNode: string;
  plProcessingExec: string;
  projectsCode: string;
  mousStatusId: string;
  prtsprTickets: string[];
  schedBlockName: string;
  receiverBand: string;
  numberQA0PassExecBlocks: number;
  drmName: string;
  preferredArc: string;
  contactScientistName: string;
  latestQa0PassExecBlockDate: string;
  firstDateReadyForProcessing: string;
  firstDateProcessing: string;
  lastDateReadyForReview: string;
  lastDateVerified: string;
  qa2Status: string;
  lastDateDelivered: string;
  stagingTimeDays: number;
  assignmentTimeDays: number;
  analysisTimeDays: number;
  reviewTimeDays: number;
  deliveryTimeDays: number;
  obsToDeliveryTimeDays: number;
  pipeCalDate: string;
  manualCalDate: string;
  pipeImgDate: string;
  manualImgDate: string;
  comment: string;

  private formatDateDifference(days, noDateDiff) {
      return days == null || days == 0 ? noDateDiff : days;
  }

  constructor(res, dateNoDiff) {
    this.mousState = res.mousState;
    this.mousSubState = res.mousSubState;
    this.dataReducerName = res.dataReducerName;
    this.dataReducerArc = res.dataReducerArc;
    this.dataReducerNode = res.dataReducerNode;
    this.plProcessingExec = res.plProcessingExec;
    this.projectsCode = res.projectsCode;
    this.mousStatusId = res.mousStatusId;
    this.prtsprTickets = res.prtsprTickets == null ? "" : res.prtsprTickets;
    this.schedBlockName = res.schedBlockName;
    this.receiverBand = res.receiverBand;
    this.numberQA0PassExecBlocks = res.numberQA0PassExecBlocks;
    this.drmName = res.drmName;
    this.preferredArc = res.preferredArc;
    this.contactScientistName = res.contactScientistName;
    this.latestQa0PassExecBlockDate = res.latestQa0PassExecBlockDate;
    this.firstDateReadyForProcessing = res.firstDateReadyForProcessing;
    this.firstDateProcessing = res.firstDateProcessing;
    this.lastDateReadyForReview = res.lastDateReadyForReview;
    this.lastDateVerified = res.lastDateVerified;
    this.qa2Status = res.qa2Status;
    this.lastDateDelivered = res.lastDateDelivered;
    this.stagingTimeDays = this.formatDateDifference(res.stagingTimeDays, dateNoDiff);
    this.assignmentTimeDays = this.formatDateDifference(res.assignmentTimeDays, dateNoDiff);
    this.analysisTimeDays = this.formatDateDifference(res.analysisTimeDays, dateNoDiff);
    this.reviewTimeDays = this.formatDateDifference(res.reviewTimeDays, dateNoDiff);
    this.deliveryTimeDays = this.formatDateDifference(res.deliveryTimeDays, dateNoDiff);
    this.obsToDeliveryTimeDays = this.formatDateDifference(res.obsToDeliveryTimeDays, dateNoDiff);
    this.pipeCalDate = res.pipeCalDate;
    this.manualCalDate = res.manualCalDate;
    this.pipeImgDate = res.pipeImgDate;
    this.manualImgDate = res.manualImgDate;
    this.comment = res.comment || "";
  }
}
