import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule, PackageOpen, Plus, Edit3, Trash2, X, Folder, PlusCircle, CornerDownRight, Tags, Users, Store, RefreshCw, XCircle, CheckCircle, Truck, Hash, Package } from 'lucide-angular';

import { AdminRoutingModule } from './admin-routing.module';
import { StoreManagementComponent } from './store-management/store-management';
import { CategoryManagementComponent } from './category-management/category-management';
import { UserManagementComponent } from './user-management/user-management';
import { AdminStoresComponent } from './admin-stores/admin-stores';
import { StoreShipmentsComponent } from './store-shipments/store-shipments';

@NgModule({
  declarations: [
    StoreManagementComponent,
    CategoryManagementComponent,
    UserManagementComponent,
    AdminStoresComponent,
    StoreShipmentsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    AdminRoutingModule,
    LucideAngularModule.pick({
      PackageOpen,
      Plus,
      Edit3,
      Trash2,
      X,
      Folder,
      PlusCircle,
      CornerDownRight,
      Tags,
      Users,
      Store,
      RefreshCw,
      XCircle,
      CheckCircle,
      Truck,
      Hash,
      Package
    })
  ]
})
export class AdminModule { }
