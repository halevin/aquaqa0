import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Globals } from '../app.globals';
import { ExecBlock } from '../domain/execblock';
import { Utils } from '../app.utils';
import { Router, NavigationExtras } from '@angular/router';

@Component({
  selector: 'sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  @Input('sidebarDisplayed') sidebarDisplayed : boolean;
  @Output() onPressed = new EventEmitter<boolean>();

  execBlocks : ExecBlock[] = [];

  constructor(private globals: Globals, public utils : Utils, public router : Router){}

  ngOnInit(): void {
  }

  onToggle() : void {
    this.onPressed.emit(!this.sidebarDisplayed);
  }

  openExecBlock( execblockUid : string ) {
   console.log("Opening execBlock " + execblockUid);
   let navigationExtras: NavigationExtras = {
    queryParams: { 'uid': execblockUid }
  };
   this.router.navigate(['/execblock'], navigationExtras);
  }

}
