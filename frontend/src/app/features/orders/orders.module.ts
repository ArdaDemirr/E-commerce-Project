import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import {
  LucideAngularModule,
  Package,
  ShoppingBag,
  ShoppingCart,
  Clock,
  Truck,
  CheckCircle,
  XCircle,
  AlertCircle,
  ChevronDown,
  ChevronUp,
  List,
  Box,
} from 'lucide-angular';

import { OrdersRoutingModule } from './orders-routing.module';
import { OrderList } from './order-list/order-list';

@NgModule({
  declarations: [OrderList],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    OrdersRoutingModule,
    LucideAngularModule.pick({
      Package,
      ShoppingBag,
      ShoppingCart,
      Clock,
      Truck,
      CheckCircle,
      XCircle,
      AlertCircle,
      ChevronDown,
      ChevronUp,
      List,
      Box,
    }),
  ],
})
export class OrdersModule {}
