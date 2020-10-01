import { Component, OnInit } from '@angular/core';
import { AppService } from '../../app.service';
import { Globals } from '../../app.globals';
import { ActivatedRoute } from '@angular/router';
import * as Highcharts from 'highcharts';
import { Emitters } from 'src/app/app.emitters';

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

  public goodCoverage: any = [];
  public satisfCoverage: any = [];
  public badCoverage: any = [];
  public pointing: any = [];
  public expectedCoverage: any = [];
  public offSourceData: any = [];
  public plotTitle;
  public rectangle: any = [];
  public expectedOff: any = [];
  public spinnerVisible = true;
  public number = 0;



  constructor(private service: AppService, private emitters : Emitters, private globals: Globals) { }

  ngOnInit() {

    console.log("Coverages initialized for " + this.globals.currentExecBlockUID);

      this.service.getCoverages(this.globals.currentExecBlockUID);
      let self = this;

      this.emitters.getCoverageLoadedEmitter().subscribe(item => {
            this.globals.coverages.forEach( e => {
              e.observedFields.forEach(element => {
                this.number++;
                if ( element.executionFraction >= 0.9 ) {
                  self.goodCoverage.push([element.ra,element.dec]);
                } else if ( element.executionFraction < 0.9 && element.executionFraction > 0.5 ) {
                  self.satisfCoverage.push([element.ra,element.dec]);
                } else  {
                  self.badCoverage.push([element.ra,element.dec]);
                }
              });
              self.plotTitle = e.sourceName;
            })
            this.globals.coverages.forEach( e => {
              e.expectedFields.forEach(element => {
                self.expectedCoverage.push([element.ra,element.dec]);
              });
              self.plotTitle = e.sourceName;
            })
            this.globals.coverages.forEach( e => {
              e.offSourceFields.forEach(element => {
                self.offSourceData.push([element.ra,element.dec]);
              });
            });
            this.globals.coverages.forEach( e => {
              if ( e.rectanglePatternTarget != null ) {
                self.rectangle.push([e.rectanglePatternTarget.ra1, e.rectanglePatternTarget.dec1])
                self.rectangle.push([e.rectanglePatternTarget.ra2, e.rectanglePatternTarget.dec2])
                self.rectangle.push([e.rectanglePatternTarget.ra3, e.rectanglePatternTarget.dec3])
                self.rectangle.push([e.rectanglePatternTarget.ra4, e.rectanglePatternTarget.dec4])
                self.rectangle.push([e.rectanglePatternTarget.ra1, e.rectanglePatternTarget.dec1])
                self.expectedOff.push([e.rectanglePatternTarget.ra, e.rectanglePatternTarget.dec])
              }
            });
            console.log("Data to plot " + JSON.stringify(self.goodCoverage));
            this.spinnerVisible = false;
            Highcharts.chart('container', this.getOptions(self.goodCoverage, self.satisfCoverage, self.badCoverage,
              self.expectedCoverage, self.offSourceData, self.rectangle, self.expectedOff, 
              'Coverages for ' + self.plotTitle));
          }
        )


  }

  getOptions(goodPoints, satisfPoints, badPoints, expectedPoints, offPoints, rectangle, expectedOff, title): any {
    return {
      chart: {
        zoomType: 'xy',
        width: 700,
        height: 700
      },

      boost: {
        useGPUTranslations: true,
        usePreAllocated: true
      },

      xAxis: {
        // min: 12.5,
        // max: 12.6,
        reversed: true,
        gridLineWidth: 1,
        title: {
          text : 'RA, hours'
        }
      },

      yAxis: {
        // Renders faster when we don't have to compute min and max
        // min: 6.32,
        // max: 6.39,
        minPadding: 0,
        maxPadding: 0,
        title: {
          text : 'Dec, degrees'
        }
      },

      title: {
        text: title
      },

      legend: {
        enabled: true
      },

      series: [
        {
          type: 'scatter',
          name: 'Expected',
          color: '#4d4dff',
          data: expectedPoints,
          
          marker: {
            symbol : 'circle',
            radius: 6
          },
          tooltip: {
            followPointer: false,
            pointFormat: '[RA: {point.x:.3f}, Dec: {point.y:.3f}]'
          }
        },
        {
          type: 'scatter',
          name: 'Observed good',
          color: '#66ff33',
          data: goodPoints,
          
          marker: {
            symbol : 'circle',
            radius: 2.5
          },
          tooltip: {
            followPointer: false,
            pointFormat: '[RA: {point.x:.3f}, Dec: {point.y:.3f}]'
          }
        },
        {
          type: 'scatter',
          name: 'Observed not bad',
          color: 'yellow',
          data: satisfPoints,
          
          marker: {
            symbol : 'circle',
            radius: 2.5
          },
          tooltip: {
            followPointer: false,
            pointFormat: '[RA: {point.x:.3f}, Dec: {point.y:.3f}]'
          }
        },
        {
          type: 'scatter',
          name: 'Observed bad',
          color: 'red',
          data: badPoints,
          
          marker: {
            symbol : 'circle',
            radius: 2.5
          },
          tooltip: {
            followPointer: false,
            pointFormat: '[RA: {point.x:.3f}, Dec: {point.y:.3f}]'
          }
        },
        {
          type: 'scatter',
          name: 'Expected Off Source',
          color: 'navy',
          data: expectedOff,
          marker: {
            symbol : 'circle',
            radius: 6.0
          },
          tooltip: {
            followPointer: false,
            pointFormat: '[RA: {point.x:.3f}, Dec: {point.y:.3f}]'
          }
        },
        {
          type: 'scatter',
          name: 'Off Source',
          color: 'teal',
          data: offPoints,
          marker: {
            symbol : 'circle',
            radius: 2.5
          },
          tooltip: {
            followPointer: false,
            pointFormat: '[RA: {point.x:.3f}, Dec: {point.y:.3f}]'
          }
        },
        {
          type: 'line',
          name: 'Expected area',
          color: 'rgba(252,100,67,0.9)',
          data: rectangle,
          marker: {
            lineWidth: 2
          },
          tooltip: {
            followPointer: false,
            headerFormat: '{series.name}',
            pointFormat: '[RA: {point.x:.3f}, Dec: {point.y:.3f}]'
          }
        },        
      ]
    }
  }
}
