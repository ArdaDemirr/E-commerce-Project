import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../../core/services/order.service';

@Component({
  selector: 'app-order-list',
  standalone: false,
  templateUrl: './order-list.html',
  styleUrl: './order-list.scss',
})
export class OrderList implements OnInit {
  orders: any[] = [];
  loading = true;
  error = false;
  expandedOrderId: number | null = null;

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.orderService.getMyOrders().subscribe({
      next: (data) => {
        this.orders = data;
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      }
    });
  }

  toggleOrder(id: number): void {
    this.expandedOrderId = this.expandedOrderId === id ? null : id;
  }

  getStatusClass(status: string): string {
    switch (status?.toLowerCase()) {
      case 'pending':   return 'status-pending';
      case 'shipped':   return 'status-shipped';
      case 'completed': return 'status-completed';
      case 'cancelled': return 'status-cancelled';
      default:          return 'status-pending';
    }
  }

  getStatusLabel(status: string): string {
    switch (status?.toLowerCase()) {
      case 'pending':   return 'Beklemede';
      case 'shipped':   return 'Kargoda';
      case 'completed': return 'Tamamlandı';
      case 'cancelled': return 'İptal Edildi';
      default:          return status;
    }
  }

  getStatusIcon(status: string): string {
    switch (status?.toLowerCase()) {
      case 'pending':   return 'clock';
      case 'shipped':   return 'truck';
      case 'completed': return 'check-circle';
      case 'cancelled': return 'x-circle';
      default:          return 'package';
    }
  }
}
