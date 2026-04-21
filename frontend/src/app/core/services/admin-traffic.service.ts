import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';

export interface AdminOrderDTO {
  id: number;
  createdAt: string;
  grandTotal: number;
  paymentMethod: string;
  status: string;
  customer?: {
    id: number;
    name: string;
    surname: string;
    email: string;
  };
  items?: any[];
}

export interface AdminShipmentDTO {
  id: number;
  orderId: number;
  trackingId: string;
  status: string;
  mode: string;
  warehouse: string;
  productImportance: string;
  customer?: {
    id: number;
    name: string;
    surname: string;
    email: string;
  };
}

export interface AdminReviewDTO {
  id: number;
  productId: number;
  productName: string;
  reviewerName?: string;
  rating: number;
  comment: string;
  sentiment: string;
  createdAt: string;
}

@Injectable({ providedIn: 'root' })
export class AdminTrafficService {
  constructor(private api: ApiService) {}

  getAllOrders(): Observable<AdminOrderDTO[]> {
    return this.api.get<AdminOrderDTO[]>('/admin/traffic/orders');
  }

  getAllShipments(): Observable<AdminShipmentDTO[]> {
    return this.api.get<AdminShipmentDTO[]>('/admin/traffic/shipments');
  }

  getAllReviews(): Observable<AdminReviewDTO[]> {
    return this.api.get<AdminReviewDTO[]>('/admin/traffic/reviews');
  }
}
