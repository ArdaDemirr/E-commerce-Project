import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, TrendingUp, TrendingDown } from 'lucide-angular';

@Component({
  selector: 'app-kpi-card',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './kpi-card.component.html',
  styleUrl: './kpi-card.component.scss'
})
export class KpiCardComponent {
  @Input() label: string = '';
  @Input() value: number | string = 0;
  @Input() change: number = 0;
  @Input() icon: string = '';
  @Input() iconColor: string = '#6366F1';
  @Input() iconBg: string = '99, 102, 241';
  @Input() delay: string = '0s';
}
