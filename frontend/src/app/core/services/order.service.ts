import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { ApiService } from './api.service';
import { CartItem } from '../models/cart.model';

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
  private MOCK_ORDERS_KEY = 'dp_mock_orders';

  constructor(private api: ApiService) {}

  private get mockOrders(): any[] {
    try {
      const raw = localStorage.getItem(this.MOCK_ORDERS_KEY);
      return raw ? JSON.parse(raw) : [];
    } catch {
      return [];
    }
  }

  private saveMockOrder(order: any): void {
    const orders = this.mockOrders;
    orders.unshift(order); // Add to beginning
    localStorage.setItem(this.MOCK_ORDERS_KEY, JSON.stringify(orders));
  }

  getMyOrders(): Observable<any[]> {
    return this.api.get<any[]>('/orders/my-orders').pipe(
      map(realOrders => {
        // Mix mock orders and real orders
        return [...this.mockOrders, ...realOrders];
      }),
      catchError(() => {
        // If backend fails, at least return mock orders
        return of(this.mockOrders);
      })
    );
  }

  getOrderById(id: number): Observable<any> {
    return this.api.get<any>(`/orders/my-orders/${id}`);
  }

  placeOrder(request: PlaceOrderRequest): Observable<any> {
    return this.api.post<any>('/orders', request);
  }

  placeMockOrder(payload: PlaceOrderRequest, cartItems: CartItem[]): Observable<any> {
    const newOrder = {
      id: Math.floor(Math.random() * 90000) + 10000,
      createdAt: new Date().toISOString(),
      status: 'PENDING',
      grandTotal: cartItems.reduce((acc, item) => acc + (item.unitPrice * item.qty), 0),
      paymentMethod: payload.paymentMethod,
      store: { name: 'Mock Teslimat Mağazası' },
      items: cartItems.map(item => ({
        productName: item.name,
        quantity: item.qty,
        price: item.unitPrice
      }))
    };

    this.saveMockOrder(newOrder);
    return of({ success: true, orderId: newOrder.id });
  }
}
