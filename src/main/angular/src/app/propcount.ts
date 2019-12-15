export class PropCount<T> {
  prop : T;
  count : number;

  constructor(prop : T, count : number) {
    this.prop = prop;
    this.count = count;
  }
}

