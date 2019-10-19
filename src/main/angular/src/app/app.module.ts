import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import {HttpClientModule, HttpClientJsonpModule, HttpClientXsrfModule} from '@angular/common/http';
import { Calendar } from 'angular-material-calendar';


import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { AppService } from './app.service';
import { Globals } from './app.globals';
import { PageHeaderComponent } from './page-header/page-header.component';
import {SuiModule} from 'ng2-semantic-ui';
import {Utils} from "./app.utils";


@NgModule({
  declarations: [
    AppComponent,
    PageHeaderComponent
  ],
  imports: [
    SuiModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule,
    HttpClientJsonpModule,
    HttpClientXsrfModule
  ],
  providers: [AppService, Globals, Utils],
  bootstrap: [AppComponent]
})
export class AppModule { }
