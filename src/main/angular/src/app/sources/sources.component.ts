import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { Globals } from '../app.globals';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-sources',
  templateUrl: './sources.component.html',
  styleUrls: ['./sources.component.css']
})
export class SourcesComponent implements OnInit {

  constructor(private service : AppService, private route : ActivatedRoute, private globals : Globals) { }

  ngOnInit() {

    this.route
    .queryParams
    .subscribe( params => {
      console.log(" Params : " + JSON.stringify(params));
      if ( params != null && params.ebuid != null) {
        this.globals.currentExecBlockUID = params.ebuid;
      }

    })

      this.service.getCoverages(this.globals.currentExecBlockUID);

  }

}
