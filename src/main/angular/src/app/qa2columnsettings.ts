
export class QA2ColumnSettings {

  title: string;
  visible: boolean;
  order: number;
  sortableColumn: string;

  constructor( visible: boolean, order : number, title: string, sortableColumn: string) {
    this.visible = visible;
    this.order = order;
    this.title = title;
    this.sortableColumn = sortableColumn;
  }


}
