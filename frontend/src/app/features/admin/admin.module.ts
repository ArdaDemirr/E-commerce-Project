import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule, PackageOpen, Plus, Edit3, Trash2, X, Folder, PlusCircle, CornerDownRight, Tags, Users } from 'lucide-angular';

import { AdminRoutingModule } from './admin-routing.module';
import { StoreManagementComponent } from './store-management/store-management';
import { CategoryManagementComponent } from './category-management/category-management';
import { UserManagementComponent } from './user-management/user-management';

@NgModule({
  declarations: [
    StoreManagementComponent,
    CategoryManagementComponent,
    UserManagementComponent
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
      Users
    })
  ]
})
export class AdminModule { }
