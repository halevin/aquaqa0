import { SnooPIPage } from './app.po';

describe('snoo-pi App', () => {
  let page: SnooPIPage;

  beforeEach(() => {
    page = new SnooPIPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
