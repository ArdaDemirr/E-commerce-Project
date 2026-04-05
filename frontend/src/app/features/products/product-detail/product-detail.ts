import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Subject, takeUntil } from 'rxjs';
import { ProductService } from '../../../core/services/product.service';
import { Product } from '../../../core/models/product.model';
import { ReviewService } from '../../../core/services/review.service';
import { ProductReviewDTO } from '../../../core/models/review.model';
import { CartService } from '../../../core/services/cart.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-product-detail',
  standalone: false,
  templateUrl: './product-detail.html',
  styleUrl: './product-detail.scss',
})
export class ProductDetailComponent implements OnInit, OnDestroy {
  product: Product | null = null;
  loading = true;
  error = false;

  // Reviews
  reviews: ProductReviewDTO[] = [];
  reviewsLoading = false;
  newRating = 5;
  newComment = '';
  submittingReview = false;
  hoverRating = 0;

  private destroy$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private productService: ProductService,
    private reviewService: ReviewService,
    private cartService: CartService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const id = parseInt(idParam, 10);
      if (!isNaN(id)) {
        this.loadProduct(id);
        this.loadReviews(id);
      } else {
        this.error = true;
        this.loading = false;
      }
    } else {
      this.error = true;
      this.loading = false;
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadProduct(id: number): void {
    this.loading = true;
    this.error = false;
    this.productService
      .getProduct(id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (product) => {
          this.product = product ?? null;
          if (!product) this.error = true;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.error = true;
          this.loading = false;
          this.cdr.detectChanges();
        },
      });
  }

  loadReviews(productId: number): void {
    this.reviewsLoading = true;
    this.reviewService.getProductReviews(productId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          this.reviews = data;
          this.reviewsLoading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.reviewsLoading = false;
          this.cdr.detectChanges();
        }
      });
  }

  submitReview(): void {
    if (!this.product || !this.newComment.trim()) return;
    this.submittingReview = true;
    this.reviewService.addReview({
      productId: this.product.id,
      rating: this.newRating,
      comment: this.newComment.trim()
    }).pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        this.toastr.success('Yorumunuz başarıyla eklendi!', 'Teşekkürler 🎉');
        this.newComment = '';
        this.newRating = 5;
        this.submittingReview = false;
        this.loadReviews(this.product!.id);
      },
      error: () => {
        this.toastr.error('Yorum eklenemedi. Lütfen tekrar deneyin.', 'Hata');
        this.submittingReview = false;
      }
    });
  }

  addToCart(product: Product): void {
    if ((product.stock ?? product.stockQty ?? 0) === 0) {
      this.toastr.warning('Bu ürün stokta yok.', 'Stok Yok');
      return;
    }
    this.cartService.addItem(product);
    this.toastr.success(`"${product.name}" sepete eklendi! 🛒`, 'Sepete Eklendi', {
      timeOut: 2500,
      positionClass: 'toast-top-right',
      progressBar: true,
    });
  }

  getStars(count: number): number[] {
    return Array.from({ length: 5 }, (_, i) => i + 1);
  }

  setRating(val: number): void { this.newRating = val; }
  setHover(val: number): void  { this.hoverRating = val; }
  clearHover(): void           { this.hoverRating = 0; }

  activeStarClass(star: number): string {
    const active = this.hoverRating > 0 ? this.hoverRating : this.newRating;
    return star <= active ? 'text-amber-400' : 'text-slate-600';
  }

  getSentimentClass(sentiment: string): string {
    switch (sentiment) {
      case 'positive': return 'bg-green-500/10 text-green-400';
      case 'neutral':  return 'bg-amber-500/10 text-amber-400';
      case 'negative': return 'bg-red-500/10 text-red-400';
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

  goBack(): void { this.location.back(); }

  getStockDisplay(p: Product): number {
    return p.stock ?? p.stockQty ?? 0;
  }

  get avgRating(): number {
    if (!this.reviews.length) return 0;
    return this.reviews.reduce((s, r) => s + r.rating, 0) / this.reviews.length;
  }
}
