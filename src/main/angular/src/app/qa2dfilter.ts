import {QA2SearchOptions} from "./qa2dsearch";

export class QA2DashboardFilter {

  version : number;
  name: string;
  columns: Array<any>;
  searchOptions: QA2SearchOptions;

  constructor( version: number, name : string, columns: Array<any>, searchOptions: QA2SearchOptions) {
    this.version = version;
    this.name = name;
    this.columns = columns;
    this.searchOptions = searchOptions;
  }

}
