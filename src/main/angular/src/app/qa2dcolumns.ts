export enum QA2DbColumns {

  state = "state",
  substate = "substate",
  drname = "drname",
  drarc = "drarc",
  drnode = "drnode",
  plProcessingExec = "plProcessingExec",
  projectCode = "projectCode",
  mousId = "mousId",
  aquaLink = "aquaLink",
  tickets = "tickets",
  sbName = "sbName",
  band = "band",
  qa0PassEBs = "qa0PassEBs",
  drmname = "drmname",
  preferredArc = "preferredArc",
  contactScientist = "contactScientist",
  lastQa0PassEB = "lastQa0PassEB",
  readyForProcessingDate = "readyForProcessingDate",
  processingDate = "processingDate",
  readyForReviewDate = "readyForReviewDate",
  verified = "verified",
  qa2Status = "qa2Status",
  deliveredDate = "deliveredDate",
  staging = "staging",
  assignment = "assignment",
  analysis = "analysis",
  review = "review",
  delivery = "delivery",
  obsToDelivery = "obsToDelivery",
  pipecal = "pipecal",
  manualcal = "manualcal",
  pipeimg = "pipeimg",
  manualimg = "manualimg",
  comment = "comment"

}

export namespace QA2DbColumns {

  export function values() {
    return Object.keys(QA2DbColumns).filter(
      (type) => isNaN(<any>type) && type !== 'values'
    );
  }
}
