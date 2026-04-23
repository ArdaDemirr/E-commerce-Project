import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ReviewService } from '../../../core/services/review.service';
import { AuthService } from '../../../core/services/auth.service';
import { MyReviewDTO, ReviewRequest } from '../../../core/models/review.model';
import { ToastrService } from 'ngx-toastr';

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

  // Edit State
  showEditModal = false;
  editingReviewId: number | null = null;
  editFormData: ReviewRequest = {
    productId: 0,
    rating: 0,
    comment: ''
  };

  constructor(
    private reviewService: ReviewService,
    private authService: AuthService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef,
  ) {}

  get isCorporate(): boolean {
    return this.authService.userRole === 'CORPORATE';
  }

  ngOnInit(): void {
    this.loadReviews();
  }

  loadReviews(): void {
    this.loading = true;
    this.reviewService.getMyReviews().subscribe({
      next: (data) => {
        this.reviews = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = true;
        this.loading = false;
        this.cdr.detectChanges();
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

  openEditModal(review: MyReviewDTO): void {
    if (this.isCorporate) return;
    this.editingReviewId = review.id;
    this.editFormData = {
      productId: review.productId, // Required by ReviewRequest DTO
      rating: review.rating,
      comment: review.comment
    };
    this.showEditModal = true;
  }

  closeEditModal(): void {
    this.showEditModal = false;
    this.editingReviewId = null;
  }

  saveReview(): void {
    if (this.isCorporate) return;
    
    if (!this.editFormData.rating || !this.editFormData.comment) {
      this.toastr.warning('Lütfen geçerli bir puan ve yorum girin.');
      return;
    }

    if (this.editingReviewId) {
      this.reviewService.updateReview(this.editingReviewId, this.editFormData).subscribe({
        next: () => {
          this.toastr.success('Yorum başarıyla güncellendi.');
          this.closeEditModal();
          this.loadReviews();
        },
        error: () => this.toastr.error('Yorum güncellenemedi.')
      });
    }
  }

  deleteReview(id: number): void {
    if (this.isCorporate) return;
    
    if (confirm('Bu yorumu silmek istediğinize emin misiniz?')) {
      this.reviewService.deleteReview(id).subscribe({
        next: () => {
          this.toastr.success('Yorum silindi.');
          this.loadReviews();
        },
        error: () => this.toastr.error('Yorum silinirken bir hata oluştu.')
      });
    }
  }
}
