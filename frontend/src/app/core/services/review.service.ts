import { Injectable, Inject, forwardRef } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { AuthService } from './auth.service';
import { MyReviewDTO, ProductReviewDTO, ReviewRequest } from '../models/review.model';

@Injectable({ providedIn: 'root' })
export class ReviewService {

  constructor(
    private api: ApiService,
    @Inject(forwardRef(() => AuthService)) private authService: AuthService
  ) {}

  getMyReviews(): Observable<MyReviewDTO[]> {
    if (this.authService.userRole === 'CORPORATE') {
      return this.api.get<MyReviewDTO[]>('/corporate/reviews');
    }
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
