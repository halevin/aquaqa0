import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Globals } from '../app.globals';
import { AppService } from '../app.service';
import { ExecBlockComment } from '../domain/comment';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit {

  @Input('comment') comment : ExecBlockComment;
  @Output() commentChange = new EventEmitter<ExecBlockComment>();

  readOnly = true;

  constructor( public globals : Globals, private service : AppService, private modalService: NgbModal) { }

  ngOnInit() {
  }

  edit(){
    this.readOnly = false;
  }

  delete(deleteconfirmation){
    this.modalService.open(deleteconfirmation, { centered: true })
    .result.then(() => {
      this.service.deleteComment(this.comment);
    }, () => {
    });    

  }

  save() {
    this.service.updateComment(this.comment);
  }

  cancel() {
    if ( this.comment.id == null ) {
      this.globals.comments.splice(0, 1);
    } else {
      this.readOnly = true;
    }
  }

}
