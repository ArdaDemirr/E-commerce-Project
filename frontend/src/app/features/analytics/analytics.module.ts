import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AnalyticsRoutingModule } from './analytics-routing.module';
import { AnalyticsPage } from './analytics-page/analytics-page';
import { LucideAngularModule, BarChart2, Star, MessageSquare, TrendingUp, ShoppingBag, Truck, Store, Users, AlertTriangle, PackageMinus, CheckCircle } from 'lucide-angular';


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
      MessageSquare,
      TrendingUp,
      ShoppingBag,
      Truck,
      Store,
      Users,
      AlertTriangle,
      PackageMinus,
      CheckCircle
    })
  ]
})
export class AnalyticsModule { }
