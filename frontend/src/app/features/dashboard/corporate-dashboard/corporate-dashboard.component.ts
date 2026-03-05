import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BaseChartDirective } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { LucideAngularModule } from 'lucide-angular';
import { AuthService } from '../../../core/services/auth.service';
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

  revenueChartData: ChartData<'line'> = {
    labels: ['Pzt', 'Sal', 'Çar', 'Per', 'Cum', 'Cmt', 'Paz'],
    datasets: [{
      label: 'Gelir', data: [1200, 1900, 1500, 2200, 1800, 2500, 3100],
      borderColor: '#6366F1', backgroundColor: 'rgba(99,102,241,0.1)',
      borderWidth: 2, fill: true, tension: 0.4,
      pointBackgroundColor: '#6366F1', pointRadius: 4, pointHoverRadius: 6,
    }]
  };

  revenueChartOptions: ChartOptions<'line'> = {
    responsive: true, maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: { backgroundColor: '#1E293B', borderColor: '#6366F1', borderWidth: 1, titleColor: '#F1F5F9', bodyColor: '#94A3B8' }
    },
    scales: {
      x: { grid: { color: 'rgba(51,65,85,0.5)' }, ticks: { color: '#64748B' } },
      y: { grid: { color: 'rgba(51,65,85,0.5)' }, ticks: { color: '#64748B' } }
    }
  };

  categoryChartData: ChartData<'doughnut'> = {
    labels: ['Elektronik', 'Moda', 'Ev', 'Diğer'],
    datasets: [{ data: [45, 25, 20, 10], backgroundColor: ['#6366F1', '#8B5CF6', '#F43F5E', '#cbd5e1'], borderWidth: 0 }]
  };

  categoryChartOptions: ChartOptions<'doughnut'> = {
    responsive: true, maintainAspectRatio: false,
    plugins: { legend: { display: false } }, cutout: '75%'
  };

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.user = this.authService.currentUser;
    this.kpis = [
      { label: 'Toplam Gelir', value: '₺24,500', change: 12.5, icon: 'pie-chart', iconColor: '#6366F1', iconBg: '99, 102, 241' },
      { label: 'Siparişler', value: '1,248', change: 8.2, icon: 'shopping-bag', iconColor: '#8B5CF6', iconBg: '139, 92, 246' },
      { label: 'Aktif Müşteriler', value: '842', change: -2.4, icon: 'users', iconColor: '#F43F5E', iconBg: '244, 63, 94' },
      { label: 'Dönüşüm Oranı', value: '3.6%', change: 1.1, icon: 'zap', iconColor: '#10B981', iconBg: '16, 185, 129' },
    ];
    this.categories = [
      { name: 'Elektronik', percentage: 45, color: '#6366F1' },
      { name: 'Moda', percentage: 25, color: '#8B5CF6' },
      { name: 'Ev & Yaşam', percentage: 20, color: '#F43F5E' },
      { name: 'Diğer', percentage: 10, color: '#cbd5e1' },
    ];
  }

  setRevenuePeriod(period: string): void { this.revenuePeriod = period; }
}
