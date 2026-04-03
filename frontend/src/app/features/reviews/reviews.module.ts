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
} from 'lucide-angular';

import { ReviewsRoutingModule } from './reviews-routing.module';
import { ReviewListComponent } from './review-list/review-list';

@NgModule({
  declarations: [ReviewListComponent],
  imports: [
    CommonModule,
    RouterModule,
    ReviewsRoutingModule,
    LucideAngularModule.pick({
      MessageSquare,
      Package,
      AlertCircle,
      Star,
      ThumbsUp,
    }),
  ],
})
export class ReviewsModule {}
