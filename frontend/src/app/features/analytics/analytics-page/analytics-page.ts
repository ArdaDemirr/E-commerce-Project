import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { StoreProductService } from '../../../core/services/store-product.service';
import { StoreProductsResponseDTO } from '../../../core/models/store-product.model';
import { AuthService } from '../../../core/services/auth.service';
import { AdminTrafficService, AdminAnalyticsDTO } from '../../../core/services/admin-traffic.service';

@Component({
  selector: 'app-analytics-page',
  standalone: false,
  templateUrl: './analytics-page.html',
  styleUrl: './analytics-page.scss',
})
export class AnalyticsPage implements OnInit {
  isAdmin = false;

  // ── Corporate state ────────────────────────────────────────────
  mostReviewedProducts: StoreProductsResponseDTO[] = [];
  highestRatedProducts: StoreProductsResponseDTO[] = [];
  lowStockProducts: StoreProductsResponseDTO[] = [];
  loadingMostReviewed = true;
  loadingHighestRated = true;
  loadingLowStock = true;

  // ── Admin state ─────────────────────────────────────────────────
  analytics: AdminAnalyticsDTO | null = null;
  loadingAnalytics = true;

  constructor(
    private storeProductService: StoreProductService,
    private authService: AuthService,
    private adminTrafficService: AdminTrafficService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.userRole === 'ADMIN';
    this.isAdmin ? this.loadAdminAnalytics() : this.loadCorporateAnalytics();
  }

  private loadAdminAnalytics(): void {
    this.adminTrafficService.getAnalytics().subscribe({
      next: (data) => {
        this.analytics = data;
        this.loadingAnalytics = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loadingAnalytics = false;
        this.cdr.detectChanges();
      }
    });
  }

  private loadCorporateAnalytics(): void {
    this.storeProductService.getMyTopReviewedProducts(0, 5).subscribe({
      next: (data) => {
        this.mostReviewedProducts = data || [];
        this.loadingMostReviewed = false;
        this.cdr.detectChanges();
      },
      error: () => { this.loadingMostReviewed = false; this.cdr.detectChanges(); }
    });

    this.storeProductService.getMyHighestRatedProducts(0, 5).subscribe({
      next: (data) => {
        this.highestRatedProducts = data || [];
        this.loadingHighestRated = false;
        this.cdr.detectChanges();
      },
      error: () => { this.loadingHighestRated = false; this.cdr.detectChanges(); }
    });

    this.storeProductService.getAllProducts().subscribe({
      next: (products) => {
        this.lowStockProducts = (products || [])
          .filter(p => p.stock <= 10)
          .sort((a, b) => a.stock - b.stock);
        this.loadingLowStock = false;
        this.cdr.detectChanges();
      },
      error: () => { this.loadingLowStock = false; this.cdr.detectChanges(); }
    });
  }
}
