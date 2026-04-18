import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { MyReviewDTO, ProductReviewDTO, ReviewRequest } from '../models/review.model';

@Injectable({ providedIn: 'root' })
export class ReviewService {

  constructor(private api: ApiService) {}

  getMyReviews(): Observable<MyReviewDTO[]> {
    return this.api.get<MyReviewDTO[]>('/reviews');
  }

  getProductReviews(productId: number): Observable<ProductReviewDTO[]> {
    return this.api.get<ProductReviewDTO[]>(`/reviews/product/${productId}`);
  }

  addReview(request: ReviewRequest): Observable<any> {
    return this.api.post<any>('/reviews', request);
  }

  getReviewById(id: number): Observable<any> {
    return this.api.get<any>(`/reviews/${id}`);
  }

  updateReview(id: number, request: ReviewRequest): Observable<any> {
    return this.api.put<any>(`/reviews/${id}`, request);
  }

  deleteReview(id: number): Observable<void> {
    return this.api.delete<void>(`/reviews/${id}`);
  }
}
