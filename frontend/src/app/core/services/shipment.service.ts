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
}
