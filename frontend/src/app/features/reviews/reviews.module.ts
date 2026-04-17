import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import {
  LucideAngularModule,
  MessageSquare,
  Package,
  AlertCircle,
  Star,
  ThumbsUp,
  Edit,
  Trash2,
  X,
} from 'lucide-angular';
import { FormsModule } from '@angular/forms';

import { ReviewsRoutingModule } from './reviews-routing.module';
import { ReviewListComponent } from './review-list/review-list';

@NgModule({
  declarations: [ReviewListComponent],
  imports: [
    CommonModule,
    RouterModule,
    ReviewsRoutingModule,
    FormsModule,
    LucideAngularModule.pick({
      MessageSquare,
      Package,
      AlertCircle,
      Star,
      ThumbsUp,
      Edit,
      Trash2,
      X,
    }),
  ],
})
export class ReviewsModule {}
