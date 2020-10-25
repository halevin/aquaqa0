import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AppService } from '../app.service';
import { Globals } from '../app.globals';
import { Utils } from '../app.utils';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Emitters } from '../app.emitters';
import { QA0Reason } from '../domain/qareason';
import { ExecBlockComment } from '../domain/comment';

@Component({
  selector: 'app-execblock',
  templateUrl: './execblock.component.html',
  styleUrls: ['./execblock.component.css']
})
export class ExecblockComponent implements OnInit {

  execBlockUID : string;
  qa0Status : string;
  finalComment : string;
  qa0ReasonsList : QA0Reason[] = [];
  qa0ReasonsSelected : string[] = [];

  executionFraction : number;
  executionFractionReport : string = null;
  execFractionSpinnerVisible : boolean = false;


  constructor(private activatedRoute : ActivatedRoute, private service : AppService, 
    public globals : Globals, public utils : Utils, private modalService: NgbModal, private emitters : Emitters) {  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params => {
      this.execBlockUID = params['uid'];
      this.globals.currentExecBlockUID = this.execBlockUID;
      this.globals.execBlockSpinnerVisible = true;
      console.log("Received " + this.execBlockUID);
      this.service.getExecBlock(this.execBlockUID);
      this.service.getAosCheck(this.execBlockUID);
    });

    this.emitters.getExecutionFractionEmitter().subscribe(item => {
        if ( item != null ) {
          this.executionFractionReport = item.executionFractionReport;
          this.executionFraction = Math.round(item.executionFraction*1000)/1000;
        }
        this.execFractionSpinnerVisible = false;
      }
    )

    this.emitters.getReloadCommentEmitter().subscribe(() => {
      this.service.getComments(this.execBlockUID);
    }
  )

}

  doQA0(contentdoqa0) {
    console.log("Do QA0");
    this.qa0Status = this.globals.execBlock.qa0Status;
    this.finalComment = this.globals.execBlock.finalComment;
    this.executionFraction = this.globals.execBlock.execFraction;
    let self = this;
    this.modalService.open(contentdoqa0, { centered: true, scrollable: true, windowClass: 'modal-do-qa0' })
    .result.then((result) => {
      self.globals.execBlock.qa0Status = self.qa0Status;
      self.globals.execBlock.finalComment = self.finalComment;
      self.globals.execBlock.execFraction = self.executionFraction;
      self.service.updateExecBlock(self.globals.execBlock);
  
    }, (reason) => {
    });    
  }

  qa0StatusChange(){
    this.qa0ReasonsList = [];
    this.qa0ReasonsSelected = [];
    if ( this.qa0Status == 'Pending' ) {
      this.qa0ReasonsList = this.globals.qa0PendingReasons;
    } else if ( this.qa0Status == 'SemiPass' || this.qa0Status == 'Fail' ) {
      this.qa0ReasonsList = this.globals.qa0SemiPassReasons;
    }

  }

  qa0ReasonsChange(){
    console.log("qa0ReasonsSelected " + JSON.stringify(this.qa0ReasonsSelected));
  }

  closeExecutionFractionReport(){
    this.executionFractionReport = null;
  }

  computeExecutionFraction(){
    this.execFractionSpinnerVisible = true;
    this.service.getExecutionFraction(this.globals.execBlock.execBlockUid);
  }  

  qa0StatusColor() : string{
    if ( this.globals.execBlock != null && this.globals.execBlock.qa0Status != null ) {
      if ( this.globals.execBlock.qa0Status == 'Pass' ) {
        return "btn-success";
      } else if ( this.globals.execBlock.qa0Status == 'Pending' ) {
        return "btn-info";
      } else if ( this.globals.execBlock.qa0Status == 'Fail' ) {
        return "btn-danger";
      } else if ( this.globals.execBlock.qa0Status == 'SemiPass' ) {
        return "btn-warning";
      } else if ( this.globals.execBlock.qa0Status == 'Unset' ) {
        return "btn-secondary";
      }
    }
    return "";
  }

  openAtmosphere(content) {
    this.modalService.open(content, { centered: true, scrollable: true, windowClass: 'modal-plots' });
  }

  openCoverages(content) {
    this.modalService.open(content, { centered: true, scrollable: true, windowClass: 'modal-plots' });
  }

  openWindow(content){
    this.modalService.open(content, { centered: true, scrollable: true, windowClass: 'modal-plots' });
  }

  openQA0ReportPDF(){
    this.service.getQA0Report(this.globals.currentExecBlockUID, "PDF");
  }

  newComment(){
    let newComment = new ExecBlockComment();
    newComment.author = this.globals.account.username;
    newComment.timestamp = new Date();
    newComment.execBlockUid = this.globals.currentExecBlockUID;
    this.globals.comments = [newComment].concat(this.globals.comments);
  }

}
