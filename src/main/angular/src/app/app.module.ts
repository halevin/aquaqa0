import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import {HttpClientModule, HttpClientJsonpModule, HttpClientXsrfModule} from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { AppService } from './app.service';
import { Globals } from './app.globals';
import { PageHeaderComponent } from './page-header/page-header.component';
import {SuiModule} from 'ng2-semantic-ui';
import { ContextMenuModule, ContextMenuService } from 'ngx-contextmenu';
import {Utils} from "./app.utils";
import { NgSelectModule } from '@ng-select/ng-select';
import {Qa2DashboardGlobals} from "./app.qa2dashboard.globals";
import { SortableColumnComponent } from './sortable-column/sortable-column.component';
import { SortableTableDirective } from './app.sortable-table.directive';
import { SortService } from './app.sort-service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { SourcesComponent } from './sources/sources.component';
import { HomeComponent } from './home/home.component';

@NgModule({
  declarations: [
    AppComponent,
    PageHeaderComponent,
    SortableTableDirective,
    SortableColumnComponent,
    SourcesComponent,
    HomeComponent,
  ],
  imports: [
    SuiModule,
    ContextMenuModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    NgbModule,
    FontAwesomeModule,
    HttpClientModule,
    HttpClientJsonpModule,
    HttpClientXsrfModule,
    NgSelectModule,
    NgbModule
  ],
  providers: [AppService, Globals, Utils, Qa2DashboardGlobals, SortService, ContextMenuService],
  bootstrap: [AppComponent]
})
export class AppModule { }
