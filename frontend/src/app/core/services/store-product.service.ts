import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { StoreProductsRequestDTO, StoreProductsResponseDTO } from '../models/store-product.model';

@Injectable({ providedIn: 'root' })
export class StoreProductService {
  constructor(private api: ApiService) {}

  getAllProducts(): Observable<StoreProductsResponseDTO[]> {
    return this.api.get<StoreProductsResponseDTO[]>('/corporate/products');
  }

  addProduct(product: StoreProductsRequestDTO): Observable<StoreProductsResponseDTO> {
    return this.api.post<StoreProductsResponseDTO>('/corporate/products', product);
  }

  updateProduct(id: number, product: StoreProductsRequestDTO): Observable<StoreProductsResponseDTO> {
    return this.api.put<StoreProductsResponseDTO>(`/corporate/products/${id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.api.delete<void>(`/corporate/products/${id}`);
  }
}
