import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';
import { AuthService } from '../../../core/services/auth.service';

interface MenuItem {
  path: string;
  label: string;
  icon: string;
  badge?: string;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, LucideAngularModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent implements OnInit {
  @Input() isCollapsed = false;
  @Output() toggleSidebar = new EventEmitter<void>();

  mainMenuItems: MenuItem[] = [];
  managementItems: MenuItem[] = [];
  showManagement = false;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(() => {
      this.buildMenu();
    });
    this.buildMenu();
  }

  buildMenu(): void {
    const role = this.authService.userRole;

    this.mainMenuItems = [
      { path: '/dashboard', label: 'Dashboard', icon: 'layout-dashboard' },
      { path: '/products', label: 'Ürünler', icon: 'package' }
    ];

    if (role === 'INDIVIDUAL') {
      this.mainMenuItems.push(
        { path: '/cart', label: 'Sepet', icon: 'shopping-cart' },
        { path: '/orders', label: 'Siparişlerim', icon: 'shopping-bag' },
        { path: '/shipments', label: 'Kargo Takibi', icon: 'truck' },
        { path: '/reviews', label: 'Yorumlarım', icon: 'star' },
        { path: '/analytics', label: 'Analizlerim', icon: 'pie-chart' }
      );
      this.showManagement = false;
    } else if (role === 'CORPORATE') {
      this.mainMenuItems.push(
        { path: '/orders', label: 'Siparişler', icon: 'shopping-bag' },
        { path: '/analytics', label: 'Analizler', icon: 'bar-chart-2' },
        { path: '/reviews', label: 'Yorumlar', icon: 'star' }
      );
      this.managementItems = [
        { path: '/admin/store-management', label: 'Mağaza Ayarları', icon: 'settings' }
      ];
      this.showManagement = true;
    } else if (role === 'ADMIN') {
      this.mainMenuItems.push(
        { path: '/analytics', label: 'Analizler', icon: 'bar-chart-2' }
      );
      this.managementItems = [
        { path: '/admin/user-management', label: 'Kullanıcılar', icon: 'users' },
        { path: '/admin/category-management', label: 'Kategoriler', icon: 'tags' },
        { path: '/admin/store-management', label: 'Mağazalar', icon: 'store' }
      ];
      this.showManagement = true;
    }
  }
}
