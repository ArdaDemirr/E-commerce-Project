import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { AdminCategoryService } from '../../../core/services/admin-category.service';
import { CategoryResponseDTO, AdminCategoryRequestDTO } from '../../../core/models/admin-category.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  standalone: false,
  selector: 'app-category-management',
  templateUrl: './category-management.html',
  styleUrl: './category-management.scss'
})
export class CategoryManagementComponent implements OnInit, OnDestroy {
  categories: CategoryResponseDTO[] = [];
  loading = true;
  private destroy$ = new Subject<void>();

  // Modal State
  showModal = false;
  isEditMode = false;
  editingId: number | null = null;
  formData: AdminCategoryRequestDTO = {
    categoryName: '',
    parentCategoryId: null
  };

  constructor(
    private categoryService: AdminCategoryService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadCategories(): void {
    this.loading = true;
    this.categoryService.getAllCategories()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          this.categories = data;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.toastr.error('Kategoriler yüklenemedi.');
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  }

  openAddModal(parentId?: number): void {
    this.isEditMode = false;
    this.editingId = null;
    this.formData = { categoryName: '', parentCategoryId: parentId || null };
    this.showModal = true;
  }

  openEditModal(cat: CategoryResponseDTO): void {
    this.isEditMode = true;
    this.editingId = cat.id;
    this.formData = {
      categoryName: cat.categoryName,
      parentCategoryId: cat.parentId || null
    };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  saveCategory(): void {
    if (!this.formData.categoryName.trim()) {
      this.toastr.warning('Kategori adı boş olamaz.');
      return;
    }

    if (this.isEditMode && this.editingId) {
      // Check if trying to set parent to itself
      if (this.formData.parentCategoryId === this.editingId) {
        this.toastr.warning('Bir kategori kendi alt kategorisi olamaz.');
        return;
      }

      this.categoryService.updateCategory(this.editingId, this.formData).subscribe({
        next: () => {
          this.toastr.success('Kategori güncellendi.');
          this.closeModal();
          this.loadCategories();
        },
        error: () => this.toastr.error('Kategori güncellenirken hata oluştu.')
      });
    } else {
      this.categoryService.createCategory(this.formData).subscribe({
        next: () => {
          this.toastr.success('Yeni kategori eklendi.');
          this.closeModal();
          this.loadCategories();
        },
        error: () => this.toastr.error('Kategori eklenirken hata oluştu.')
      });
    }
  }

  deleteCategory(id: number): void {
    if (confirm('Bu kategoriyi silmek istediğinize emin misiniz? Alt gruplar da silinebilir veya etkilenebilir.')) {
      this.categoryService.deleteCategory(id).subscribe({
        next: () => {
          this.toastr.success('Kategori silindi.');
          this.loadCategories();
        },
        error: () => this.toastr.error('Kategori silinemedi. (Belki içerisine ait ürünler vardır)')
      });
    }
  }

  // Gets top level categories
  get rootCategories(): CategoryResponseDTO[] {
    return this.categories.filter(c => !c.parentId);
  }

  // Gets children of a specific category
  getChildren(parentId: number): CategoryResponseDTO[] {
    return this.categories.filter(c => c.parentId === parentId);
  }
}
