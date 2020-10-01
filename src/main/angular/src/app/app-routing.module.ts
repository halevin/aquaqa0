import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SearchComponent } from './search/search.component';
import { ExecblockComponent } from './execblock/execblock.component';
import { DashboardComponent } from './dashboard/dashboard.component';


const routes: Routes = [
  {
    path: 'search',
    component: SearchComponent
  },
  {
    path: 'execblock',
    component: ExecblockComponent
  },
  {
    path: 'execblock/:uid',
    component: ExecblockComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent
  },
  {
    path: '',
    redirectTo: 'search', pathMatch: 'full'
  },
  {
    path: '**',
    component: SearchComponent
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes,
    { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
