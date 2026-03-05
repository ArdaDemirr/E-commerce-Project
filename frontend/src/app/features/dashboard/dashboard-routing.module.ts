import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CorporateDashboardComponent } from './corporate-dashboard/corporate-dashboard.component';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';

const routes: Routes = [
  { path: '', component: CorporateDashboardComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: [provideCharts(withDefaultRegisterables())]
})
export class DashboardRoutingModule { }
