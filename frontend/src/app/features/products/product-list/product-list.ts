import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';
import { ProductService } from '../../../core/services/product.service';
import { Product, Category } from '../../../core/models/product.model';
import { CartService } from '../../../core/services/cart.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  standalone: false,
  selector: 'app-product-list',
  templateUrl: './product-list.html',
  styleUrl: './product-list.scss',
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
  pageSize = 50;

  private destroy$ = new Subject<void>();
  private searchSubject = new Subject<string>();

  get totalProducts(): number {
    return this.filteredProducts.length;
  }
  get isLastPage(): boolean {
    return (this.currentPage + 1) * this.pageSize >= this.totalProducts;
  }
  get pagedProducts(): Product[] {
    const start = this.currentPage * this.pageSize;
    return this.filteredProducts.slice(start, start + this.pageSize);
  }

  constructor(
    private productService: ProductService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private cartService: CartService,
    private toastr: ToastrService,
    public authService: AuthService,
  ) {}

  ngOnInit(): void {
    // Debounced search
    this.searchSubject
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe(() => {
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
    this.productService
      .getCategories()
      .pipe(takeUntil(this.destroy$))
      .subscribe((cats) => {
        this.categories = cats;
        this.cdr.detectChanges();
      });
  }

  loadProducts(): void {
    this.loading = true;
    this.productService
      .getProducts()
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
        },
      });
  }

  applyFilters(): void {
    let result = [...this.products];

    // Category filter
    if (this.selectedCategoryId !== null) {
      result = result.filter((p) => p.category?.id === this.selectedCategoryId);
    }

    // Search query filter
    const q = this.searchQuery.trim().toLowerCase();
    if (q) {
      result = result.filter(
        (p) =>
          p.name.toLowerCase().includes(q) ||
          p.sku?.toLowerCase().includes(q) ||
          p.categoryName?.toLowerCase().includes(q),
      );
    }

    // Sort
    switch (this.sortBy) {
      case 'price_asc':
        result.sort((a, b) => a.unitPrice - b.unitPrice);
        break;
      case 'price_desc':
        result.sort((a, b) => b.unitPrice - a.unitPrice);
        break;
      default:
        break;
    }

    this.filteredProducts = result;
  }

  selectCategory(id: number | null): void {
    this.selectedCategoryId = id;
    this.currentPage = 0;
    this.loading = true; // Trigger skeleton loading

    if (id === null) {
      // Fetch all products
      this.loadProducts();
    } else {
      // Fetch products specific to that category using the proper backend endpoint
      this.productService
        .getProductsByCategory(id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (products) => {
            this.products = products;
            this.applyFilters(); // will apply sort/search
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: () => {
            this.products = [];
            this.filteredProducts = [];
            this.loading = false;
            this.cdr.detectChanges();
          },
        });
    }
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
    if ((product.stock ?? product.stockQty ?? 0) === 0) {
      this.toastr.warning('Bu ürün stokta yok.', 'Stok Yok');
      return;
    }
    this.cartService.addItem(product);
    this.toastr.success(
      `"${product.name}" sepete eklendi! 🛒`,
      'Sepete Eklendi',
      {
        timeOut: 2500,
        positionClass: 'toast-top-right',
        progressBar: true,
      },
    );
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.cdr.detectChanges();
      this.scrollToTop();
    }
  }

  nextPage(): void {
    if (!this.isLastPage) {
      this.currentPage++;
      this.cdr.detectChanges();
      this.scrollToTop();
    }
  }

  private scrollToTop(): void {
    const mainContainer = document.querySelector('main');
    if (mainContainer) {
      mainContainer.scrollTo({ top: 0, behavior: 'smooth' });
    } else {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  getStockDisplay(p: Product): number {
    return p.stock ?? p.stockQty ?? 0;
  }
}
