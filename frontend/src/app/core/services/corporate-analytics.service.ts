import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';

export interface CorporateAnalyticsDTO {
  totalEarnings: number;
  totalOrders: number;
  totalProducts: number;
}

@Injectable({ providedIn: 'root' })
export class CorporateAnalyticsService {
  constructor(private api: ApiService) {}

  getDashboardAnalytics(): Observable<CorporateAnalyticsDTO> {
    return this.api.get<CorporateAnalyticsDTO>('/corporate/analytics/dashboard');
  }
}
