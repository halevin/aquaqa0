import {EventEmitter, Injectable} from '@angular/core';


@Injectable()
export class Emitters {

  expandSidebar: EventEmitter<number> = new EventEmitter();
  hideSidebar: EventEmitter<number> = new EventEmitter();
  change: EventEmitter<number> = new EventEmitter();
  loadingOn: EventEmitter<number> = new EventEmitter();
  loadingOff: EventEmitter<number> = new EventEmitter();

  antennaFlagsLoadingEmitter : EventEmitter<number> = new EventEmitter();

  coverageLoaded: EventEmitter<number> = new EventEmitter();
  atmosphereLoaded: EventEmitter<number> = new EventEmitter();
  executionFractionEmitter: EventEmitter<number> = new EventEmitter();


  getExpandSideBarEmitter() {
    return this.expandSidebar;
  }

  getHideSideBarEmitter() {
    return this.hideSidebar;
  }

  getChangeEmitter() {
    return this.change;
  }

  getLoadingOnEmitter() {
    return this.loadingOn;
  }

  getLoadingOffEmitter() {
    return this.loadingOff;
  }

  getCoverageLoadedEmitter() {
    return this.coverageLoaded;
  }

  getAtmosphereLoadedEmitter() {
    return this.atmosphereLoaded;
  }

  getExecutionFractionEmitter(){
    return this.executionFractionEmitter;
  }

  getAntennaFlagsLoadingEmitter(){
    return this.antennaFlagsLoadingEmitter;
  }


}




