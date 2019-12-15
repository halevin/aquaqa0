export class TimeStatistic {
  caption : string;
  median : number;
  min : number;
  max : number;

  constructor(caption : string, median : number, min : number, max : number) {
    this.caption = caption;
    this.median = median;
    this.min = min;
    this.max = max;
  }
}
