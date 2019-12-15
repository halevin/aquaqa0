import { Component, OnInit, Input, OnDestroy, HostListener } from '@angular/core';
import { Subscription } from 'rxjs';
import { SortService } from '../app.sort-service';

@Component({
    selector: '[sortable-column]',
    templateUrl: './sortable-column.component.html'
})
export class SortableColumnComponent implements OnInit, OnDestroy {

    constructor(private sortService: SortService) { }

    @Input('sortable-column')
    columnName: string;

    @Input('sort-direction')
    sortDirection: string = 'asc';

    @Input('sort-table')
    tableName: string;

    private columnSortedSubscription: Subscription;

    @HostListener('click')
    sort() {
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
        this.sortService.columnSorted({ sortColumn: this.columnName, sortDirection: this.sortDirection }, this.tableName);
    }

    ngOnInit() {
        // subscribe to sort changes so we can react when other columns are sorted
        if (this.tableName) {
            this.columnSortedSubscription = this.sortService.columnSorted$.get(parseInt(this.tableName)).subscribe(event => {
                // reset this column's sort direction to hide the sort icons
                if (this.columnName != event.sortColumn) {
                    this.sortDirection = '';
                }
            });
        }
    }

    ngOnDestroy() {
        this.columnSortedSubscription.unsubscribe();
    }
}
