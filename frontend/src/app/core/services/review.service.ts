import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { MyReviewDTO, ProductReviewDTO, ReviewRequest } from '../models/review.model';

@Injectable({ providedIn: 'root' })
export class ReviewService {

  constructor(private api: ApiService) {}

  getMyReviews(): Observable<MyReviewDTO[]> {
    return this.api.get<MyReviewDTO[]>('/reviews/my-reviews');
  }

  getProductReviews(productId: number): Observable<ProductReviewDTO[]> {
    return this.api.get<ProductReviewDTO[]>(`/reviews/product/${productId}`);
  }

  addReview(request: ReviewRequest): Observable<any> {
    return this.api.post<any>('/reviews', request);
  }
}
