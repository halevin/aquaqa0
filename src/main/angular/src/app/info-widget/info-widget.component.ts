import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'info-widget',
  templateUrl: './info-widget.component.html',
  styleUrls: ['./info-widget.component.css']
})
export class InfoWidgetComponent implements OnInit {

  @Input('link') link : string;
  @Input('title') title : string;
  @Input('icon') icon : string;
  @Input('iconcolor') iconcolor : string;



  constructor() { }

  ngOnInit() {
  }

}
