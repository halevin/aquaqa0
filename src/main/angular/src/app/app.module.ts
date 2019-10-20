import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import {HttpClientModule, HttpClientJsonpModule, HttpClientXsrfModule} from '@angular/common/http';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './home/home.component';
import { NavigationSidebarComponent } from './navigation-sidebar/navigation-sidebar.component';
import { AppService } from './app.service';
import { PageHeaderComponent } from './page-header/page-header.component';
import { Globals } from './app.globals';
import { Render } from './app.render';
import { InfoWidgetComponent } from './info-widget/info-widget.component';
import { CookieService } from 'ngx-cookie-service';
import { SortableColumnComponent } from './sortable-column/sortable-column.component';
import { SortableTableDirective } from './app.sortable-table.directive';
import { SortService } from './app.sort-service';
import {SuiModule} from 'ng2-semantic-ui';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NavigationSidebarComponent,
    PageHeaderComponent,
    InfoWidgetComponent,
    SortableTableDirective,
    SortableColumnComponent
  ],
  imports: [
    SuiModule,
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    HttpClientJsonpModule,
    HttpClientXsrfModule
  ],
  providers: [AppService, Globals, CookieService, Render, SortService],
  bootstrap: [AppComponent]
})
export class AppModule { }
