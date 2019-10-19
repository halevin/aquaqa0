import { NgModule }              from '@angular/core';
import { RouterModule, Routes }  from '@angular/router';

const appRoutes: Routes = [
//     {
//       path: 'pipeline',
//       component: PipelineComponent
//     },
//     {
//       path: 'assign',
//       component: AssignComponent
//     },
//     {
//       path: 'manual',
//       component: ManualComponent
//     },
//     {
//       path: '**',
//       component: ManualComponent
//     }
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
