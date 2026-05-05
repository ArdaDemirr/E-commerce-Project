import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShipmentTrackComponent } from './shipment-track/shipment-track';

const routes: Routes = [
  { path: '', component: ShipmentTrackComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ShipmentsRoutingModule {}
