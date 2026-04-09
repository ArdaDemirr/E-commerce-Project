import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StoreManagementComponent } from './store-management/store-management';
import { CategoryManagementComponent } from './category-management/category-management';

const routes: Routes = [
  { path: 'store-management', component: StoreManagementComponent },
  { path: 'category-management', component: CategoryManagementComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
