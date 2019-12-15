import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { Qa2DashboardGlobals } from "./app.qa2dashboard.globals";
import {TableName} from "./tablename";

@Injectable()
export class SortService {

    constructor(public qa2DashboardGlobals: Qa2DashboardGlobals) { }

    columnSortedSources : Map<TableName, Subject<ColumnSortedEvent>> =
        new Map(this.qa2DashboardGlobals.TABLE_NAMES.map(table =>
          [table, new Subject()] as [TableName, Subject<ColumnSortedEvent>])
        );

    columnSorted$ : Map<TableName, Observable<ColumnSortedEvent>> =
        new Map(this.qa2DashboardGlobals.TABLE_NAMES.map(table =>
          [table, this.columnSortedSources.get(table).asObservable()] as [TableName, Observable<ColumnSortedEvent>])
        );

    columnSorted(event: ColumnSortedEvent, table: string) {
        this.columnSortedSources.get(parseInt(table)).next(event);
    }

}

export interface ColumnSortedEvent {
    sortColumn: string;
    sortDirection: string;
}
