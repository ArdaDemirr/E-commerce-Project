import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';
import { AuthService } from '../../../core/services/auth.service';
import { CartService } from '../../../core/services/cart.service';
import { Observable } from 'rxjs';

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

  cartCount$: Observable<number>;

  constructor(private authService: AuthService, private cartService: CartService) {
    this.cartCount$ = this.cartService.count$;
  }

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(() => {
      this.buildMenu();
    });
    this.buildMenu();
  }

  buildMenu(): void {
    const role = this.authService.userRole;

    this.mainMenuItems = [
      { path: '/products', label: 'Ürünler', icon: 'package' },
      { path: '/chat', label: 'Asistan (AI)', icon: 'bot' }
    ];

    if (role === 'INDIVIDUAL') {
      this.mainMenuItems.push(
        { path: '/cart', label: 'Sepet', icon: 'shopping-cart' },
        { path: '/orders', label: 'Siparişlerim', icon: 'shopping-bag' },
        { path: '/shipments', label: 'Kargo Takibi', icon: 'truck' },
        { path: '/reviews', label: 'Yorumlarım', icon: 'star' }
      );
      this.showManagement = false;
    } else if (role === 'CORPORATE') {
      this.mainMenuItems.unshift({ path: '/dashboard', label: 'Dashboard', icon: 'layout-dashboard' });
      this.mainMenuItems.push(
        { path: '/analytics', label: 'Analizler', icon: 'bar-chart-2' }
      );
      this.managementItems = [
        { path: '/admin/store-management', label: 'Mağaza Ürünleri', icon: 'settings' },
        { path: '/orders', label: 'Siparişler', icon: 'shopping-bag' },
        { path: '/reviews', label: 'Yorumlar', icon: 'star' },
        { path: '/admin/store-shipments', label: 'Gönderi Yönetimi', icon: 'truck' }
      ];
      this.showManagement = true;
    } else if (role === 'ADMIN') {
      this.mainMenuItems = [
        { path: '/analytics', label: 'Analizler', icon: 'bar-chart-2' },
        { path: '/products', label: 'Ürünler', icon: 'package' },
        { path: '/chat', label: 'Asistan (AI)', icon: 'bot' }
      ];
      this.managementItems = [
        { path: '/admin/user-management', label: 'Kullanıcılar', icon: 'users' },
        { path: '/admin/category-management', label: 'Kategoriler', icon: 'tags' },
        { path: '/admin/stores', label: 'Mağazalar', icon: 'store' },
        { path: '/admin/traffic', label: 'Site Akışı', icon: 'activity' }
      ];
      this.showManagement = true;
    }
  }
}
