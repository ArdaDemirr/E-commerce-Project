import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
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

  getProductByIdAndStoreId(id: number): Observable<StoreProductsResponseDTO> {
    return this.api.get<StoreProductsResponseDTO>(`/corporate/products/${id}`);
  }

  deleteProduct(id: number): Observable<void> {
    return this.api.delete<void>(`/corporate/products/${id}`);
  }

  getMyTopReviewedProducts(page: number = 0, size: number = 5): Observable<StoreProductsResponseDTO[]> {
    return this.api.get<any>(`/corporate/products/analytics/most-reviewed?page=${page}&size=${size}`).pipe(
      map(res => res.content ? res.content : res)
    );
  }

  getMyHighestRatedProducts(page: number = 0, size: number = 5): Observable<StoreProductsResponseDTO[]> {
    return this.api.get<any>(`/corporate/products/analytics/highest-rated?page=${page}&size=${size}`).pipe(
      map(res => res.content ? res.content : res)
    );
  }
}
