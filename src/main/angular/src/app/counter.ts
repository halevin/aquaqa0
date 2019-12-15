// List of all Counters for individual columns
// Update counterAssocs when changing this
import {TableName} from "./tablename";
import {QA2DashboardEntry} from "./qa2dashboardentry";

export enum Counter {
  MousStates,
  MousSubStates,
  DataReducerNames,
  DataReducerArcs,
  DataReducerNodes,
  PLProcessingExecs,
  Bands,
  DRMs,
  PreferredArcs,
  ContactScientists,
  QA2Statuses,
}

export namespace Counter {

  const counterAssocs: Map<Counter, [string, (ous: QA2DashboardEntry) => string, TableName]> = new Map([
    //       Counter              Caption                        Entry field                     Table
    [Counter.MousStates,        ["State",             ous => ous.mousState,            TableName.MousStates as TableName]],
    [Counter.MousSubStates,     ["Substate",          ous => ous.mousSubState,         TableName.MousSubStates]],
    [Counter.DataReducerNames,  ["Data Reducer Name", ous => ous.dataReducerName,      TableName.DataReducerNames]],
    [Counter.DataReducerArcs,   ["Data Reducer Arc",  ous => ous.dataReducerArc,       TableName.DataReducerArcs]],
    [Counter.DataReducerNodes,  ["Data Reducer Node", ous => ous.dataReducerNode,      TableName.DataReducerNodes]],
    [Counter.PLProcessingExecs, ["PL proc. exec.",    ous => ous.plProcessingExec,     TableName.PLProcessingExecs]],
    [Counter.Bands,             ["Band",              ous => ous.receiverBand,         TableName.Bands]],
    [Counter.DRMs,              ["DRM",               ous => ous.drmName,              TableName.DRMs]],
    [Counter.PreferredArcs,     ["Preferred Arc",     ous => ous.preferredArc,         TableName.PreferredArcs]],
    [Counter.ContactScientists, ["CS",                ous => ous.contactScientistName, TableName.ContactScientists]],
    [Counter.QA2Statuses,       ["QA2 Status",        ous => ous.qa2Status,            TableName.QA2Statuses]],
  ]);

  export function captionForCounter(counter: Counter): string {
    return counterAssocs.get(counter)[0];
  }

  export function propForCounter(counter: Counter, ous: QA2DashboardEntry): string {
    return counterAssocs.get(counter)[1](ous);
  }

  export function tableForCounter(counter: Counter): TableName {
    return counterAssocs.get(counter)[2];
  }

}
