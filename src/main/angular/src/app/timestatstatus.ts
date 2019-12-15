// List of all date differences about which statistics (median, min, max) are displayed
// Update timestatAssocs when changing this
import {QA2DashboardEntry} from "./qa2dashboardentry";

export enum TimestatStatus {
  Staging,
  Assignment,
  Analysis,
  Review,
  Delivery,
  ObsToDelivery,
}

export namespace TimestatStatus {

  const timestatAssocs: Map<TimestatStatus, [string, (ous: QA2DashboardEntry) => number]> = new Map([
    //              TimestatStatus   Caption              Entry field
    [TimestatStatus.Staging,       ["Staging", ous => ous.stagingTimeDays]],
    [TimestatStatus.Assignment,    ["Assignment", ous => ous.assignmentTimeDays]],
    [TimestatStatus.Analysis,      ["Analysis", ous => ous.analysisTimeDays]],
    [TimestatStatus.Review,        ["Review", ous => ous.reviewTimeDays]],
    [TimestatStatus.Delivery,      ["Delivery", ous => ous.deliveryTimeDays]],
    [TimestatStatus.ObsToDelivery, ["Obs. to Delivery", ous => ous.obsToDeliveryTimeDays]],
  ]);

  export function captionForTimestat(timestat: TimestatStatus): string {
    return timestatAssocs.get(timestat)[0];
  }

  export function propForTimestat(timestat: TimestatStatus, ous: QA2DashboardEntry): number | string {
    return timestatAssocs.get(timestat)[1](ous);
  }

}
