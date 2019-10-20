import { NgModule }              from '@angular/core';
import { RouterModule, Routes }  from '@angular/router';
import { HomeComponent } from './home/home.component';

const appRoutes: Routes = [
/*    {
      path: 'project-list/:mode',
      component: ProjectListComponent
    },
*/
    {
      path: '**',
      component: HomeComponent
    }
  ];

  @NgModule({
    imports: [
      RouterModule.forRoot(
        appRoutes,
        { useHash: true } // <-- debugging purposes only
      )
    ],
    exports: [
      RouterModule
    ]
  })
  export class AppRoutingModule {}
