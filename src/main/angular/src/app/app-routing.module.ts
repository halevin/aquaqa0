import { NgModule }              from '@angular/core';
import { RouterModule, Routes }  from '@angular/router';
import { SourcesComponent } from './sources/sources.component';
import { HomeComponent } from './home/home.component';

const appRoutes: Routes = [
    {
      path: 'sources',
      component: SourcesComponent
    },
    {
      path: '**',
      component: HomeComponent
    }
  ];

  @NgModule({
    imports: [
      RouterModule.forRoot(
        appRoutes,
        { useHash: true}
      )
    ],
    exports: [
      RouterModule
    ]
  })
  export class AppRoutingModule {}
