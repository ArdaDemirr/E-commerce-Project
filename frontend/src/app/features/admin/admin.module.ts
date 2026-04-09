import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule, PackageOpen, Plus, Edit3, Trash2, X, Folder, PlusCircle, CornerDownRight, Tags } from 'lucide-angular';

import { AdminRoutingModule } from './admin-routing.module';
import { StoreManagementComponent } from './store-management/store-management';
import { CategoryManagementComponent } from './category-management/category-management';

@NgModule({
  declarations: [
    StoreManagementComponent,
    CategoryManagementComponent
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
      Tags
    })
  ]
})
export class AdminModule { }
