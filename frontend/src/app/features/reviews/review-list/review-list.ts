import { Component, OnInit } from '@angular/core';
import { ReviewService } from '../../../core/services/review.service';
import { MyReviewDTO } from '../../../core/models/review.model';

@Component({
  selector: 'app-review-list',
  standalone: false,
  templateUrl: './review-list.html',
  styleUrl: './review-list.scss',
})
export class ReviewListComponent implements OnInit {
  reviews: MyReviewDTO[] = [];
  loading = true;
  error = false;

  constructor(private reviewService: ReviewService) {}

  ngOnInit(): void {
    this.reviewService.getMyReviews().subscribe({
      next: (data) => {
        this.reviews = data;
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      }
    });
  }

  getStars(rating: number): number[] {
    return Array.from({ length: 5 }, (_, i) => i + 1);
  }

  getSentimentClass(sentiment: string): string {
    switch (sentiment) {
      case 'positive': return 'sentiment-positive';
      case 'neutral':  return 'sentiment-neutral';
      case 'negative': return 'sentiment-negative';
      default:         return '';
    }
  }

  getSentimentLabel(sentiment: string): string {
    switch (sentiment) {
      case 'positive': return 'Olumlu';
      case 'neutral':  return 'Nötr';
      case 'negative': return 'Olumsuz';
      default:         return sentiment;
    }
  }
}
