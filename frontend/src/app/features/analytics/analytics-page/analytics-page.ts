import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { StoreProductService } from '../../../core/services/store-product.service';
import { StoreProductsResponseDTO } from '../../../core/models/store-product.model';

@Component({
  selector: 'app-analytics-page',
  standalone: false,
  templateUrl: './analytics-page.html',
  styleUrl: './analytics-page.scss',
})
export class AnalyticsPage implements OnInit {
  mostReviewedProducts: StoreProductsResponseDTO[] = [];
  highestRatedProducts: StoreProductsResponseDTO[] = [];
  loadingMostReviewed = true;
  loadingHighestRated = true;

  constructor(
    private storeProductService: StoreProductService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.storeProductService.getMyTopReviewedProducts(0, 5).subscribe({
      next: (data) => {
        this.mostReviewedProducts = data || [];
        this.loadingMostReviewed = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loadingMostReviewed = false;
        this.cdr.detectChanges();
      }
    });

    this.storeProductService.getMyHighestRatedProducts(0, 5).subscribe({
      next: (data) => {
        this.highestRatedProducts = data || [];
        this.loadingHighestRated = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loadingHighestRated = false;
        this.cdr.detectChanges();
      }
    });
  }
}
