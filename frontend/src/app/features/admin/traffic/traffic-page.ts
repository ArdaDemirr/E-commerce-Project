import { Component, OnInit, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { AdminTrafficService, AdminOrderDTO, AdminShipmentDTO, AdminReviewDTO } from '../../../core/services/admin-traffic.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-traffic-page',
  standalone: false,
  templateUrl: './traffic-page.html',
  styleUrl: './traffic-page.scss'
})
export class TrafficPageComponent implements OnInit, OnDestroy {
  activeTab: 'orders' | 'shipments' | 'reviews' = 'orders';
  
  orders: AdminOrderDTO[] = [];
  shipments: AdminShipmentDTO[] = [];
  reviews: AdminReviewDTO[] = [];
  
  loading = true;
  private destroy$ = new Subject<void>();

  constructor(
    private trafficService: AdminTrafficService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.switchTab('orders');
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  switchTab(tab: 'orders' | 'shipments' | 'reviews'): void {
    this.activeTab = tab;
    this.loading = true;
    this.cdr.detectChanges();

    if (tab === 'orders') {
      this.trafficService.getAllOrders().pipe(takeUntil(this.destroy$)).subscribe({
        next: (data) => {
          this.orders = data || [];
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
    } else if (tab === 'shipments') {
      this.trafficService.getAllShipments().pipe(takeUntil(this.destroy$)).subscribe({
        next: (data) => {
          this.shipments = data || [];
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
    } else if (tab === 'reviews') {
      this.trafficService.getAllReviews().pipe(takeUntil(this.destroy$)).subscribe({
        next: (data) => {
          this.reviews = data || [];
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
    }
  }

  getStars(count: number): number[] {
    return Array.from({ length: 5 }, (_, i) => i + 1);
  }

  getSentimentClass(sentiment: string): string {
    switch (sentiment) {
      case 'positive': return 'bg-green-500/10 text-green-400 border-green-500/20';
      case 'neutral':  return 'bg-amber-500/10 text-amber-400 border-amber-500/20';
      case 'negative': return 'bg-red-500/10 text-red-400 border-red-500/20';
      default:         return 'bg-slate-500/10 text-slate-400';
    }
  }
}
