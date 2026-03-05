import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule } from 'lucide-angular';

import { ProductsRoutingModule } from './products-routing.module';
import { ProductListComponent } from './product-list/product-list';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    ProductListComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ProductsRoutingModule,
    SharedModule,
    LucideAngularModule
  ]
})
export class ProductsModule { }
