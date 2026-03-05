import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';
import { Product } from '../../../core/models/product.model';

@Component({
  standalone: false,
  selector: 'app-product-list',
  templateUrl: './product-list.html',
  styleUrl: './product-list.scss'
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  loading = false;
  viewMode: 'grid' | 'list' = 'grid';
  searchQuery = '';
  sortBy = 'newest';
  currentPage = 0;
  pageSize = 12;
  totalProducts = 0;

  get isLastPage(): boolean {
    return (this.currentPage + 1) * this.pageSize >= this.totalProducts;
  }

  constructor(private api: ApiService, private router: Router) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    // Mock data for now — replace with API call
    setTimeout(() => {
      this.products = Array.from({ length: 12 }, (_, i) => ({
        id: i + 1,
        name: `Ürün ${i + 1}`,
        sku: `SKU-${1000 + i}`,
        description: 'Açıklama',
        unitPrice: Math.floor(Math.random() * 500) + 50,
        stockQty: Math.floor(Math.random() * 100) + 1,
        categoryName: ['Elektronik', 'Moda', 'Ev', 'Spor'][i % 4],
        avgRating: +(Math.random() * 2 + 3).toFixed(1),
      } as any));
      this.totalProducts = 48;
      this.loading = false;
    }, 600);
  }

  onSearch(): void {
    this.currentPage = 0;
    this.loadProducts();
  }

  viewProduct(id: number): void {
    this.router.navigate(['/products', id]);
  }

  addToCart(product: any, event: Event): void {
    event.stopPropagation();
    // TODO: Dispatch NgRx addToCart action
    console.log('Add to cart:', product.name);
  }

  prevPage(): void {
    if (this.currentPage > 0) { this.currentPage--; this.loadProducts(); }
  }

  nextPage(): void {
    if (!this.isLastPage) { this.currentPage++; this.loadProducts(); }
  }
}
