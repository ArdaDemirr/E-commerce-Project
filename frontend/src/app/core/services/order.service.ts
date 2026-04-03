import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';

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

  constructor(private api: ApiService) {}

  getMyOrders(): Observable<any[]> {
    return this.api.get<any[]>('/orders/my-orders');
  }

  placeOrder(request: PlaceOrderRequest): Observable<any> {
    return this.api.post<any>('/orders', request);
  }
}
