import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { ShipmentResponseDTO } from '../models/shipment.model';

@Injectable({ providedIn: 'root' })
export class ShipmentService {

  constructor(private api: ApiService) {}

  getShipmentByOrderId(orderId: number): Observable<ShipmentResponseDTO> {
    return this.api.get<ShipmentResponseDTO>(`/shipments/order/${orderId}`);
  }

  createShipment(orderId: number): Observable<ShipmentResponseDTO> {
    return this.api.post<ShipmentResponseDTO>('/shipments/create', orderId);
  }

  getMyStoreShipments(): Observable<ShipmentResponseDTO[]> {
    return this.api.get<ShipmentResponseDTO[]>('/shipments/my-shipments');
  }

  updateShipmentStatus(id: number, status: string): Observable<ShipmentResponseDTO> {
    return this.api.put<ShipmentResponseDTO>(`/shipments/${id}/status`, { status });
  }
}
