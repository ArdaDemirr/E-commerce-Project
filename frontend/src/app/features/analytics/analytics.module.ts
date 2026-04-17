import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AnalyticsRoutingModule } from './analytics-routing.module';
import { AnalyticsPage } from './analytics-page/analytics-page';
import { LucideAngularModule, BarChart2, Star, MessageSquare } from 'lucide-angular';


@NgModule({
  declarations: [
    AnalyticsPage
  ],
  imports: [
    CommonModule,
    AnalyticsRoutingModule,
    LucideAngularModule.pick({
      BarChart2,
      Star,
      MessageSquare
    })
  ]
})
export class AnalyticsModule { }
