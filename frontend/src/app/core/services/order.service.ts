import { Injectable, Inject, forwardRef } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { AuthService } from './auth.service';

export interface OrderItemRequest {
  productId: number;
  quantity: number;
}

export interface PlaceOrderRequest {
  storeId: number;
  paymentMethod: string;
  items: OrderItemRequest[];
}

@Injectable({ providedIn: 'root' })
export class OrderService {
  constructor(
    private api: ApiService,
    @Inject(forwardRef(() => AuthService)) private authService: AuthService
  ) {}

  getMyOrders(): Observable<any[]> {
    if (this.authService.userRole === 'CORPORATE') {
      return this.api.get<any[]>('/corporate/orders');
    }
    return this.api.get<any[]>('/orders');
  }

  getOrderById(id: number): Observable<any> {
    if (this.authService.userRole === 'CORPORATE') {
      return this.api.get<any>(`/corporate/orders/${id}`);
    }
    return this.api.get<any>(`/orders/${id}`);
  }

  placeOrder(request: PlaceOrderRequest): Observable<any> {
    return this.api.post<any>('/orders', request);
  }
}
