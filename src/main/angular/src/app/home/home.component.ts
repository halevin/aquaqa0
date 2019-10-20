import { Component, OnInit } from '@angular/core';
import {AppService} from '../app.service';
import {Globals} from '../app.globals';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  subscription: any;
  search : string = "Projects";
  searchString : string;
  isDesc : boolean = true;
  column : any;
  direction : number = 1;
  private sub:any;

  constructor(private appService: AppService, public globals: Globals, private router: Router, private route: ActivatedRoute) { }

  ngOnInit():void {
    this.sub = this.route.params.subscribe(params => {
      this.globals.mode = params['mode'];
  });
//    this.globals.mode = this.globals.modes.pi;
  }

  setSearchProjects(){
    this.search = "Projects";
  }

  setSearchSB(){
    this.search = "SchedBlocks";
  }

  sort(property){
    this.isDesc = !this.isDesc; //change the direction
    this.column = property;
    this.direction = this.isDesc ? 1 : -1;
    //this.sort(this.column);
  };

  doSearch(){
    this.globals.searchString = this.searchString;
    if (this.search=="Projects"){
      this.router.navigate(['/project-list/'+this.globals.mode+'/'+this.searchString.toLowerCase()]);
    } else if (this.search=="SchedBlocks"){
      this.router.navigate(['/sched-blocks/'+this.globals.mode+'/'+this.searchString.toLowerCase()]);
    }
  }

  sum( a: number, b: number) : number {
    if ( a == null && b == null ) return 0;
    if ( a == null ) return b;
    if ( b == null ) return a;
    return (Number(a)+Number(b));
  }

}
