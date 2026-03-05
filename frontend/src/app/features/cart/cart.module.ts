import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule } from 'lucide-angular';

import { CartRoutingModule } from './cart-routing.module';
import { CartPageComponent } from './cart-page/cart-page';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    CartPageComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    CartRoutingModule,
    SharedModule,
    LucideAngularModule
  ]
})
export class CartModule { }
