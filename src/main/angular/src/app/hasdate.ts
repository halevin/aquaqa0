// List of all columns for which we check how many entries have a date (rather than being empty)
// update hasDateAssocs when changing this
import {QA2DashboardEntry} from "./qa2dashboardentry";

export enum HasDate {
  PipeCal,
  ManualCal,
  PipeImg,
  ManualImg,
}

export namespace HasDate {
  const hasDateAssocs: Map<HasDate, [string, (ous: QA2DashboardEntry) => string]> = new Map([
    //       HasDate      Caption                    Entry field
    [HasDate.PipeCal,   ["Has Pipecal",   ous => ous.pipeCalDate]],
    [HasDate.ManualCal, ["Has Manualcal", ous => ous.manualCalDate]],
    [HasDate.PipeImg,   ["Has Pipeimg",   ous => ous.pipeImgDate]],
    [HasDate.ManualImg, ["Has Manualimg", ous => ous.manualImgDate]],
  ]);

  export function captionForHasDate(hasDate: HasDate): string {
    return hasDateAssocs.get(hasDate)[0];
  }

  export function dateExistsForHasDate(hasDate: HasDate, ous: QA2DashboardEntry): boolean {
    return hasDateAssocs.get(hasDate)[1](ous) !== this.NO_DATE;
  }

}
