import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import {
  LucideAngularModule,
  Package,
  ShoppingBag,
  Truck,
  Ship,
  Plane,
  Clock,
  Info,
  Search,
  Activity,
  Warehouse,
  ShieldCheck,
  ScanBarcode,
  ClipboardCopy,
  Check,
  Home,
} from 'lucide-angular';

import { ShipmentsRoutingModule } from './shipments-routing.module';
import { ShipmentTrackComponent } from './shipment-track/shipment-track';

@NgModule({
  declarations: [ShipmentTrackComponent],
  imports: [
    CommonModule,
    RouterModule,
    ShipmentsRoutingModule,
    LucideAngularModule.pick({
      Package,
      ShoppingBag,
      Truck,
      Ship,
      Plane,
      Clock,
      Info,
      Search,
      Activity,
      Warehouse,
      ShieldCheck,
      ScanBarcode,
      ClipboardCopy,
      Check,
      Home,
    }),
  ],
})
export class ShipmentsModule {}
