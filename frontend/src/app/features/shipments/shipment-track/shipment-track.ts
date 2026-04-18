import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ShipmentService } from '../../../core/services/shipment.service';
import { ShipmentResponseDTO } from '../../../core/models/shipment.model';
import { OrderService } from '../../../core/services/order.service';

@Component({
  selector: 'app-shipment-track',
  standalone: false,
  templateUrl: './shipment-track.html',
  styleUrl: './shipment-track.scss',
})
export class ShipmentTrackComponent implements OnInit {
  // Orders list for selecting which order to track
  orders: any[] = [];
  ordersLoading = true;

  // Currently selected order's shipment
  selectedOrderId: number | null = null;
  shipment: ShipmentResponseDTO | null = null;
  shipmentLoading = false;
  notFound = false;

  constructor(
    private shipmentService: ShipmentService,
    private orderService: OrderService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.orderService.getMyOrders().subscribe({
      next: (data) => {
        // En yeni sipariş/kargo en üstte olacak şekilde id'ye göre azalan sıralama yapıyoruz
        this.orders = data.sort((a: any, b: any) => b.id - a.id);
        this.ordersLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.ordersLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  selectOrder(orderId: number): void {
    this.selectedOrderId = orderId;
    this.shipment = null;
    this.notFound = false;
    this.shipmentLoading = true;
    this.cdr.detectChanges();

    this.shipmentService.getShipmentByOrderId(orderId).subscribe({
      next: (data) => {
        this.shipment = data;
        this.shipmentLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.shipmentLoading = false;
        this.notFound = true;
        this.cdr.detectChanges();
      }
    });
  }

  getModeIcon(mode: string): string {
    switch (mode?.toLowerCase()) {
      case 'flight': case 'air': return 'plane';
      case 'ship':               return 'ship';
      default:                   return 'truck';
    }
  }

  getModeLabel(mode: string): string {
    switch (mode?.toLowerCase()) {
      case 'flight': case 'air': return 'Uçak Kargo';
      case 'ship':               return 'Deniz Kargo';
      default:                   return 'Kara Kargo';
    }
  }

  getStatusClass(status: string): string {
    switch (status?.toLowerCase()) {
      case 'pending':
      case 'preparing':  return 'bg-amber-500/15 text-amber-400';
      case 'shipped':    return 'bg-blue-500/15 text-blue-400';
      case 'delivered':  return 'bg-green-500/15 text-green-400';
      case 'cancelled':  return 'bg-red-500/15 text-red-400';
      default:           return 'bg-slate-500/15 text-slate-400';
    }
  }

  getStatusLabel(status: string): string {
    switch (status?.toLowerCase()) {
      case 'pending':
      case 'preparing': return 'Hazırlanıyor';
      case 'shipped':   return 'Yolda / Kargoya Verildi';
      case 'delivered': return 'Teslim Edildi';
      case 'cancelled': return 'İptal';
      default:          return status || '—';
    }
  }

  getImportanceClass(importance: string): string {
    switch (importance?.toLowerCase()) {
      case 'high':   return 'text-red-400 bg-red-500/10';
      case 'medium': return 'text-amber-400 bg-amber-500/10';
      default:       return 'text-slate-400 bg-slate-500/10';
    }
  }

  getImportanceLabel(importance: string): string {
    switch (importance?.toLowerCase()) {
      case 'high':   return 'Yüksek';
      case 'medium': return 'Orta';
      case 'low':    return 'Düşük';
      default:       return importance || '—';
    }
  }

  getOrderStatusLabel(status: string): string {
    switch (status?.toLowerCase()) {
      case 'pending':   return 'Beklemede';
      case 'shipped':   return 'Kargoda';
      case 'completed': return 'Tamamlandı';
      case 'cancelled': return 'İptal';
      default:          return status;
    }
  }
}
