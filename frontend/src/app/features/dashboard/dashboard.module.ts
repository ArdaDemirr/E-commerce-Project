import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BaseChartDirective, provideCharts, withDefaultRegisterables } from 'ng2-charts';
import { LucideAngularModule, Download, Plus } from 'lucide-angular';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [],
  imports: [
    DashboardRoutingModule,
    RouterModule,
    SharedModule,
    BaseChartDirective,
    LucideAngularModule.pick({ Download, Plus }),
  ],
  providers: [
    provideCharts(withDefaultRegisterables())
  ]
})
export class DashboardModule { }
