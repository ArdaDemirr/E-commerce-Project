import { Component, OnInit } from '@angular/core';
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

  constructor(private storeProductService: StoreProductService) {}

  ngOnInit(): void {
    this.storeProductService.getMyTopReviewedProducts(0, 5).subscribe({
      next: (data) => {
        this.mostReviewedProducts = data;
        this.loadingMostReviewed = false;
      },
      error: () => this.loadingMostReviewed = false
    });

    this.storeProductService.getMyHighestRatedProducts(0, 5).subscribe({
      next: (data) => {
        this.highestRatedProducts = data;
        this.loadingHighestRated = false;
      },
      error: () => this.loadingHighestRated = false
    });
  }
}
