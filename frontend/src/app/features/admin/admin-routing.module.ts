import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StoreManagementComponent } from './store-management/store-management';
import { CategoryManagementComponent } from './category-management/category-management';
import { UserManagementComponent } from './user-management/user-management';
import { AdminStoresComponent } from './admin-stores/admin-stores';
import { StoreShipmentsComponent } from './store-shipments/store-shipments';
import { TrafficPageComponent } from './traffic/traffic-page';

const routes: Routes = [
  { path: 'store-management', component: StoreManagementComponent },
  { path: 'category-management', component: CategoryManagementComponent },
  { path: 'user-management', component: UserManagementComponent },
  { path: 'stores', component: AdminStoresComponent },
  { path: 'store-shipments', component: StoreShipmentsComponent },
  { path: 'traffic', component: TrafficPageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
