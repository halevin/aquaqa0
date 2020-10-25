import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PageHeaderComponent } from './page-header/page-header.component';
import { AppService } from './app.service';
import { Globals } from './app.globals';
import { Utils } from './app.utils';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SidebarComponent } from './sidebar/sidebar.component';
import { HttpClientModule, HttpClientJsonpModule, HttpClientXsrfModule } from '@angular/common/http';
import { SearchComponent } from './search/search.component';
import { ExecblockComponent } from './execblock/execblock.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { HighchartsChartModule } from 'highcharts-angular';
import { SourcesComponent } from './details/sources/sources.component';
import { AtmosphereComponent } from './details/atmosphere/atmosphere.component';
import { Emitters } from './app.emitters';
import { Processors } from './app.processors';
import { Qa0StatusHistoryComponent } from './details/qa0-status-history/qa0-status-history.component';
import { CustomDatePipe } from './app.datepipe';
import { AntennaFlagsComponent } from './antenna-flags/antenna-flags.component';
import { CommentComponent } from './comment/comment.component';
import { AutofocusDirective } from 'src/autofocus.directive';


@NgModule({
  declarations: [
    CustomDatePipe,
    AppComponent,
    PageHeaderComponent,
    SidebarComponent,
    SearchComponent,
    ExecblockComponent,
    DashboardComponent,
    SourcesComponent,
    AtmosphereComponent,
    Qa0StatusHistoryComponent,
    AntennaFlagsComponent,
    CommentComponent,
    AutofocusDirective
  ],
  imports: [
    BrowserModule,
    RouterModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    HttpClientJsonpModule,
    HttpClientXsrfModule,
    NgSelectModule,
    FormsModule,
    HighchartsChartModule

  ],
  providers: [AppService, Globals, Utils, Emitters, Processors],
  bootstrap: [AppComponent]
})
export class AppModule { }
