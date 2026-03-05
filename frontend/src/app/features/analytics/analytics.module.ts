import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AnalyticsRoutingModule } from './analytics-routing.module';
import { AnalyticsPage } from './analytics-page/analytics-page';


@NgModule({
  declarations: [
    AnalyticsPage
  ],
  imports: [
    CommonModule,
    AnalyticsRoutingModule
  ]
})
export class AnalyticsModule { }
