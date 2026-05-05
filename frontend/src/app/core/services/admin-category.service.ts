import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { CategoryResponseDTO, AdminCategoryRequestDTO } from '../models/admin-category.model';

@Injectable({ providedIn: 'root' })
export class AdminCategoryService {
  constructor(private api: ApiService) {}

  getAllCategories(): Observable<CategoryResponseDTO[]> {
    return this.api.get<CategoryResponseDTO[]>('/admin/categories');
  }

  createCategory(request: AdminCategoryRequestDTO): Observable<CategoryResponseDTO> {
    return this.api.post<CategoryResponseDTO>('/admin/categories', request);
  }

  updateCategory(id: number, request: AdminCategoryRequestDTO): Observable<CategoryResponseDTO> {
    return this.api.put<CategoryResponseDTO>(`/admin/categories/${id}`, request);
  }

  deleteCategory(id: number): Observable<void> {
    return this.api.delete<void>(`/admin/categories/${id}`);
  }
}
