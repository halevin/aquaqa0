import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { Globals } from '../app.globals';
import { ActivatedRoute } from '@angular/router';
import * as Highcharts from 'highcharts';

declare var require: any;
let Boost = require('highcharts/modules/boost');
let noData = require('highcharts/modules/no-data-to-display');
let More = require('highcharts/highcharts-more');

Boost(Highcharts);
noData(Highcharts);
More(Highcharts);
noData(Highcharts);


@Component({
  selector: 'app-sources',
  templateUrl: './sources.component.html',
  styleUrls: ['./sources.component.css']
})
export class SourcesComponent implements OnInit {

  public data: any = [];



  constructor(private service: AppService, private route: ActivatedRoute, private globals: Globals) { }

  ngOnInit() {

    this.route
      .queryParams
      .subscribe(params => {
        console.log(" Params : " + JSON.stringify(params));
        if (params != null && params.ebuid != null) {
          this.globals.currentExecBlockUID = params.ebuid;
          if (this.globals.currentExecBlockUID != null) {
            this.service.getCoverages(this.globals.currentExecBlockUID);
          }
        }

      })

      let self = this;

      this.service.getCoverageLoadedEmitter().subscribe(item => {
            this.globals.coverages.forEach( e => {
              e.observedFields.forEach(element => {
                self.data.push([element.ra,element.dec]);
              });
            })
        
            Highcharts.chart('container', this.getOptions(self.data));
          }
        )


//    this.testPlot();


  }

  testPlot() {

    // Prepare the data
    this.data = [];
    let n = 1000000;
    for (let i = 0; i < n; i += 1) {
      this.data.push([
        Math.pow(Math.random(), 2) * 100,
        Math.pow(Math.random(), 2) * 100
      ]);
    }

    if (!Highcharts.Series.prototype.render) {
      throw 'Module not loaded';
    }

    console.time('scatter');
    Highcharts.chart('container', this.getOptions(this.data));
    console.timeEnd('scatter');




  }

  getOptions(points): any {
    return {
      chart: {
        zoomType: 'xy',
        height: '100%'
      },

      boost: {
        useGPUTranslations: true,
        usePreAllocated: true
      },

      xAxis: {
        min: 12.5,
        max: 12.6,
        gridLineWidth: 1
      },

      yAxis: {
        // Renders faster when we don't have to compute min and max
        min: 6.32,
        max: 6.39,
        minPadding: 0,
        maxPadding: 0,
        title: {
          text: null
        }
      },

      title: {
        text: 'Scatter chart with 1 million points'
      },

      legend: {
        enabled: false
      },

      series: [{
        type: 'scatter',
        color: 'rgba(152,100,67,0.9)',
        data: points,
        marker: {
          radius: 1.5
        },
        tooltip: {
          followPointer: false,
          pointFormat: '[{point.x:.1f}, {point.y:.1f}]'
        }
      }]

    }




  }




}
