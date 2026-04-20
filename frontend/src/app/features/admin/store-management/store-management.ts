import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { StoreProductService } from '../../../core/services/store-product.service';
import { ProductService } from '../../../core/services/product.service'; // For category list
import {
  StoreProductsRequestDTO,
  StoreProductsResponseDTO,
} from '../../../core/models/store-product.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  standalone: false,
  selector: 'app-store-management',
  templateUrl: './store-management.html',
  styleUrl: './store-management.scss',
})
export class StoreManagementComponent implements OnInit, OnDestroy {
  products: StoreProductsResponseDTO[] = [];
  categories: any[] = [];
  loading = true;
  private destroy$ = new Subject<void>();

  // Modal State
  showModal = false;
  isEditMode = false;
  editingId: number | null = null;

  formData: StoreProductsRequestDTO = {
    name: '',
    sku: '',
    unitPrice: 0,
    stock: 0,
    description: '',
    categoryId: 0,
    imageUrl: '',
  };

  constructor(
    private storeProductService: StoreProductService,
    private productService: ProductService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    this.loadProducts();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadCategories(): void {
    this.productService.getCategories().subscribe((cats) => {
      this.categories = cats;
    });
  }

  loadProducts(): void {
    this.loading = true;
    this.storeProductService
      .getAllProducts()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (res) => {
          this.products = res;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.toastr.error('Ürünler yüklenirken hata oluştu.');
          this.loading = false;
          this.cdr.detectChanges();
        },
      });
  }

  openAddModal(): void {
    this.isEditMode = false;
    this.editingId = null;
    this.formData = {
      name: '',
      sku: '',
      unitPrice: 0,
      stock: 0,
      description: '',
      categoryId: this.categories[0]?.id || 0,
      imageUrl: '',
    };
    this.showModal = true;
  }

  openEditModal(product: StoreProductsResponseDTO): void {
    this.isEditMode = true;
    this.editingId = product.id;
    this.formData = {
      name: product.name,
      sku: product.sku,
      unitPrice: product.unitPrice,
      stock: product.stock,
      description: product.description,
      categoryId: product.categoryId,
      imageUrl: product.imageUrl || '',
    };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  saveProduct(): void {
    if (
      !this.formData.name ||
      !this.formData.sku ||
      !this.formData.categoryId
    ) {
      this.toastr.warning('Lütfen zorunlu alanları doldurun.');
      return;
    }

    if (this.isEditMode && this.editingId) {
      this.storeProductService
        .updateProduct(this.editingId, this.formData)
        .subscribe({
          next: () => {
            this.toastr.success('Ürün başarıyla güncellendi.');
            this.closeModal();
            this.loadProducts();
          },
          error: () => this.toastr.error('Ürün güncellenemedi.'),
        });
    } else {
      this.storeProductService.addProduct(this.formData).subscribe({
        next: () => {
          this.toastr.success('Ürün başarıyla eklendi.');
          this.closeModal();
          this.loadProducts();
        },
        error: () => this.toastr.error('Ürün eklenemedi.'),
      });
    }
  }

  deleteProduct(id: number): void {
    if (confirm('Bu ürünü silmek istediğinize emin misiniz?')) {
      this.storeProductService.deleteProduct(id).subscribe({
        next: () => {
          this.toastr.success('Ürün silindi.');
          this.loadProducts();
        },
        error: () => this.toastr.error('Ürün silinemedi.'),
      });
    }
  }
}
