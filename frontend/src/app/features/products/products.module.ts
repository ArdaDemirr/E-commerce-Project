import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  LucideAngularModule,
  Grid,
  List,
  ArrowLeft,
  ShoppingCart,
  Check,
  Truck,
  Shield,
  Star,
  MessageSquare,
  Edit3,
  Send,
  ThumbsUp,
  Package,
} from 'lucide-angular';

import { ProductsRoutingModule } from './products-routing.module';
import { ProductListComponent } from './product-list/product-list';
import { ProductDetailComponent } from './product-detail/product-detail';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [ProductListComponent, ProductDetailComponent],
  imports: [
    CommonModule,
    FormsModule,
    ProductsRoutingModule,
    SharedModule,
    LucideAngularModule.pick({
      Grid,
      List,
      ArrowLeft,
      ShoppingCart,
      Check,
      Truck,
      Shield,
      Star,
      MessageSquare,
      Edit3,
      Send,
      ThumbsUp,
      Package,
    }),
  ],
})
export class ProductsModule {}
