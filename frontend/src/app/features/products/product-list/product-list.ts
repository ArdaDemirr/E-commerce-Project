import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';
import { ProductService } from '../../../core/services/product.service';
import { Product, Category } from '../../../core/models/product.model';

@Component({
  standalone: false,
  selector: 'app-product-list',
  templateUrl: './product-list.html',
  styleUrl: './product-list.scss'
})
export class ProductListComponent implements OnInit, OnDestroy {
  products: Product[] = [];
  filteredProducts: Product[] = [];
  categories: Category[] = [];
  loading = true; // başlangıçta true → skeleton göster
  viewMode: 'grid' | 'list' = 'grid';
  searchQuery = '';
  sortBy = 'newest';
  selectedCategoryId: number | null = null;
  currentPage = 0;
  pageSize = 12;

  private destroy$ = new Subject<void>();
  private searchSubject = new Subject<string>();

  get totalProducts(): number { return this.filteredProducts.length; }
  get isLastPage(): boolean { return (this.currentPage + 1) * this.pageSize >= this.totalProducts; }
  get pagedProducts(): Product[] {
    const start = this.currentPage * this.pageSize;
    return this.filteredProducts.slice(start, start + this.pageSize);
  }

  constructor(
    private productService: ProductService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // Debounced search
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      this.currentPage = 0;
      this.applyFilters();
      this.cdr.detectChanges();
    });

    this.loadCategories();
    this.loadProducts();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadCategories(): void {
    this.productService.getCategories()
      .pipe(takeUntil(this.destroy$))
      .subscribe(cats => {
        this.categories = cats;
        this.cdr.detectChanges();
      });
  }

  loadProducts(): void {
    this.loading = true;
    this.productService.getProducts()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (products) => {
          this.products = products;
          this.applyFilters();
          this.loading = false;
          this.cdr.detectChanges(); // force Angular to rerender immediately
        },
        error: () => {
          this.products = [];
          this.filteredProducts = [];
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  }

  applyFilters(): void {
    let result = [...this.products];

    // Category filter
    if (this.selectedCategoryId !== null) {
      result = result.filter(p => p.category?.id === this.selectedCategoryId);
    }

    // Search query filter
    const q = this.searchQuery.trim().toLowerCase();
    if (q) {
      result = result.filter(p =>
        p.name.toLowerCase().includes(q) ||
        (p.sku?.toLowerCase().includes(q)) ||
        (p.categoryName?.toLowerCase().includes(q))
      );
    }

    // Sort
    switch (this.sortBy) {
      case 'price_asc': result.sort((a, b) => a.unitPrice - b.unitPrice); break;
      case 'price_desc': result.sort((a, b) => b.unitPrice - a.unitPrice); break;
      default: break;
    }

    this.filteredProducts = result;
  }

  selectCategory(id: number | null): void {
    this.selectedCategoryId = id;
    this.currentPage = 0;
    this.applyFilters();
    this.cdr.detectChanges();
  }

  onSearch(): void {
    this.searchSubject.next(this.searchQuery);
  }

  onSortChange(): void {
    this.currentPage = 0;
    this.applyFilters();
    this.cdr.detectChanges();
  }

  viewProduct(id: number): void {
    this.router.navigate(['/products', id]);
  }

  addToCart(product: Product, event: Event): void {
    event.stopPropagation();
    console.log('Add to cart:', product.name);
  }

  prevPage(): void {
    if (this.currentPage > 0) { this.currentPage--; this.cdr.detectChanges(); }
  }

  nextPage(): void {
    if (!this.isLastPage) { this.currentPage++; this.cdr.detectChanges(); }
  }

  getStockDisplay(p: Product): number {
    return p.stock ?? p.stockQty ?? 0;
  }
}
