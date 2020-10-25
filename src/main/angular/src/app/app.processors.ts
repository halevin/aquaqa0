import {Injectable} from '@angular/core';
import { Emitters } from './app.emitters';
import { Globals } from './app.globals';
import { AppService } from './app.service';


@Injectable()
export class Processors {

  constructor(private globals: Globals, private emitters : Emitters){
	}



  public afterOperation(res: any, operationType:string) {
    if ( operationType == this.globals.operationType.coverages){
      this.extractCoverages(res);
    } else if (operationType==this.globals.operationType.settings) {
      this.extractSettings(res);
    } else if (operationType == this.globals.operationType.execBlocks) {
      this.extractExecBlocks(res);
    } else if (operationType == this.globals.operationType.execBlock) {
      this.extractExecBlock(res);
    } else if (operationType == this.globals.operationType.executionFraction) {
      this.extractExecutionFraction(res);
    } else if (operationType == this.globals.operationType.aoscheck) {
      this.extractAosCheck(res);
    } else if (operationType == this.globals.operationType.qa0history) {
      this.extractQA0StatusHistory(res);
    } else if (operationType == this.globals.operationType.atmosphere) {
      this.extractAtmosphere(res);
    } else if (operationType == this.globals.operationType.antennaFlags) {
      this.extractAntennaFlags(res);
    } else if (operationType == this.globals.operationType.comments) {
      this.extractComments(res);
    } else if (operationType == this.globals.operationType.updateComment) {
      this.updateComment(res);
    } else if (operationType == this.globals.operationType.deleteComment) {
      this.deleteComment(res);
    }
  }

  private extractExecBlocks( result : any) {
    console.log("ExecBlocks " + JSON.stringify(result));
    this.globals.searchSpinnerVisible = false;
    this.globals.execBlocks = result;
  }

  private extractExecBlock( result : any) {
    console.log("ExecBlock " + JSON.stringify(result));
    this.globals.execBlockSpinnerVisible = false;
    this.globals.execBlock = result;
  }

  private extractExecutionFraction( result : any) {
    console.log("ExecBlock " + JSON.stringify(result));
    this.emitters.getExecutionFractionEmitter().emit(result);
  }

  private extractAosCheck( result : any) {
    this.globals.aosCheckSummary = result;
    console.log("AosCheck " + JSON.stringify(this.globals.aosCheckSummary));
    // this.emitters.getExecutionFractionEmitter().emit(result);
  }

  private extractQA0StatusHistory(result : any) {
    this.globals.qa0StatusHitory = result;
    console.log("QA0 Status History " + JSON.stringify(this.globals.qa0StatusHitory));
  }

  private extractComments(result : any) {
    this.globals.comments = result;
    console.log("Comments " + JSON.stringify(this.globals.comments));
  }

  private updateComment(result : any) {
    this.emitters.getReloadCommentEmitter().emit();
  }

  private deleteComment(result : any) {
    this.emitters.getReloadCommentEmitter().emit();
  }

  private extractCoverages(res: any) {
    this.globals.coverages = res;
    console.log("Coveragess " + JSON.stringify(res));
    this.emitters.getCoverageLoadedEmitter().emit();
  }

  private extractAtmosphere(res: any) {
    this.globals.atmosphere = res;
    console.log("Atmosphere " + JSON.stringify(res));
    this.emitters.getAtmosphereLoadedEmitter().emit(res);
  }

  private extractAntennaFlags(res: any) {
    console.log("Antenna flags " + JSON.stringify(res));
    this.globals.antennaFlags = res;
    this.emitters.getAntennaFlagsLoadingEmitter().emit();
  }

  private extractSettings(res: any) {
    this.globals.helpURL = res.helpURL;
    this.globals.helpdeskEmail = res.email;
    this.globals.webShiftLogURL = res.webShiftLogURL;
    this.globals.protrackURL = res.protrackURL;
    this.globals.aquaQA2URL = res.aquaQA2URL;
    this.globals.qa0SemiPassReasons = res.qa0SemiPassReasons;
    this.globals.qa0PendingReasons = res.qa0PendingReasons;
    console.log("settings" + JSON.stringify(res));
  }



}
