import { Component, Input, Output, EventEmitter } from '@angular/core';
import {Globals} from '../app.globals';

@Component({
  selector: 'navigation-sidebar',
  templateUrl: './navigation-sidebar.component.html',
  styleUrls: ['./navigation-sidebar.component.css']
})
export class NavigationSidebarComponent {
  @Input('sidebarDisplayed') sidebarDisplayed : boolean;
  @Output() onPressed = new EventEmitter<boolean>();

  constructor(public globals: Globals){}

  onToggle() : void {
    this.onPressed.emit(!this.sidebarDisplayed);
  }
}
