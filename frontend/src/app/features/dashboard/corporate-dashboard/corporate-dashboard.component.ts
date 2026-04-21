import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BaseChartDirective } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { LucideAngularModule } from 'lucide-angular';
import { AuthService } from '../../../core/services/auth.service';
import { StoreProductService } from '../../../core/services/store-product.service';
import { CorporateAnalyticsService } from '../../../core/services/corporate-analytics.service';
import { User } from '../../../core/models/user.model';
import { KpiCardComponent } from '../../../shared/components/kpi-card/kpi-card.component';

@Component({
  selector: 'app-corporate-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, BaseChartDirective, LucideAngularModule, KpiCardComponent],
  templateUrl: './corporate-dashboard.component.html',
  styleUrl: './corporate-dashboard.component.scss'
})
export class CorporateDashboardComponent implements OnInit {
  user: User | null = null;
  revenuePeriod = '7G';
  kpis: any[] = [];
  categories: any[] = [];

  revenueChartData: ChartData<'bar'> = {
    labels: [],
    datasets: [{
      label: 'Ürün Fiyatı (₺)', data: [],
      backgroundColor: 'rgba(99,102,241,0.8)',
      categoryPercentage: 0.8,
      barPercentage: 0.9,
    }]
  };

  revenueChartOptions: ChartOptions<'bar'> = {
    responsive: true, maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: { backgroundColor: '#1E293B', borderColor: '#6366F1', borderWidth: 1, titleColor: '#F1F5F9', bodyColor: '#94A3B8' }
    },
    scales: {
      x: { grid: { color: 'rgba(51,65,85,0.5)' }, ticks: { color: '#64748B' } },
      y: { grid: { color: 'rgba(51,65,85,0.5)' }, ticks: { color: '#64748B' }, beginAtZero: true }
    }
  };

  categoryChartData: ChartData<'doughnut'> = {
    labels: [],
    datasets: [{ data: [], backgroundColor: ['#6366F1', '#8B5CF6', '#F43F5E', '#10B981', '#F59E0B', '#cbd5e1'], borderWidth: 0 }]
  };

  categoryChartOptions: ChartOptions<'doughnut'> = {
    responsive: true, maintainAspectRatio: false,
    plugins: { legend: { display: false } }, cutout: '75%'
  };

  constructor(
    private authService: AuthService,
    private storeProductService: StoreProductService,
    private corporateAnalyticsService: CorporateAnalyticsService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.user = this.authService.currentUser;
    this.kpis = [
      { label: 'Toplam Kazanç/Satış', value: '...', change: 0, icon: 'pie-chart', iconColor: '#6366F1', iconBg: '99, 102, 241' },
      { label: 'Toplam Sipariş', value: '...', change: 0, icon: 'shopping-bag', iconColor: '#8B5CF6', iconBg: '139, 92, 246' },
      { label: 'Toplam Ürün', value: '...', change: 0, icon: 'package', iconColor: '#F43F5E', iconBg: '244, 63, 94' }
    ];

    this.corporateAnalyticsService.getDashboardAnalytics().subscribe(data => {
      this.kpis = [
        { label: 'Toplam Kazanç/Satış', value: `₺${data.totalEarnings.toLocaleString('tr-TR', { maximumFractionDigits: 2 })}`, change: 0, icon: 'pie-chart', iconColor: '#6366F1', iconBg: '99, 102, 241' },
        { label: 'Toplam Sipariş', value: data.totalOrders.toString(), change: 0, icon: 'shopping-bag', iconColor: '#8B5CF6', iconBg: '139, 92, 246' },
        { label: 'Toplam Ürün', value: data.totalProducts.toString(), change: 0, icon: 'package', iconColor: '#F43F5E', iconBg: '244, 63, 94' }
      ];
      this.cdr.detectChanges();
    });

    // En Çok Yorumlanan Ürünler Grafiği (Bar Chart - Fiyat Karşılaştırmalı)
    this.storeProductService.getMyTopReviewedProducts(0, 5).subscribe(products => {
      if (products && products.length > 0) {
        this.revenueChartData = {
          labels: products.map(p => p.name.length > 15 ? p.name.substring(0, 15) + '...' : p.name),
          datasets: [{
            label: 'Ürün Fiyatı (₺)',
            data: products.map(p => p.unitPrice),
            backgroundColor: 'rgba(99,102,241,0.8)',
            categoryPercentage: 0.8,
            barPercentage: 0.9,
          }]
        };
        this.cdr.detectChanges();
      }
    });

    // En Yüksek Puanlı Ürünler Grafiği (Doughnut Chart - Stok Karşılaştırmalı)
    this.storeProductService.getMyHighestRatedProducts(0, 5).subscribe(products => {
      const colors = ['#6366F1', '#8B5CF6', '#F43F5E', '#10B981', '#F59E0B'];
      if (products && products.length > 0) {
        const totalStock = products.reduce((sum, p) => sum + (p.stock || 0), 0);
        this.categoryChartData = {
          labels: products.map(p => p.name),
          datasets: [{
            data: products.map(p => p.stock),
            backgroundColor: colors,
            borderWidth: 0
          }]
        };

        this.categories = products.map((p, index) => ({
          name: p.name.length > 20 ? p.name.substring(0, 20) + '...' : p.name,
          percentage: totalStock > 0 ? Math.round(((p.stock || 0) / totalStock) * 100) : 0,
          color: colors[index % colors.length]
        }));
        this.cdr.detectChanges();
      } else {
        this.categories = [];
      }
    });
  }

  setRevenuePeriod(period: string): void { this.revenuePeriod = period; }
}
