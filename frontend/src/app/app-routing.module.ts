import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';

const routes: Routes = [
  {
    path: 'auth',
    component: AuthLayoutComponent,
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule) },
      { path: 'products', loadChildren: () => import('./features/products/products.module').then(m => m.ProductsModule) },
      { path: 'cart', loadChildren: () => import('./features/cart/cart.module').then(m => m.CartModule) },
      { path: 'orders', loadChildren: () => import('./features/orders/orders.module').then(m => m.OrdersModule) },
      { path: 'shipments', loadChildren: () => import('./features/shipments/shipments.module').then(m => m.ShipmentsModule) },
      { path: 'reviews', loadChildren: () => import('./features/reviews/reviews.module').then(m => m.ReviewsModule) },
      { path: 'analytics', loadChildren: () => import('./features/analytics/analytics.module').then(m => m.AnalyticsModule) },
      {
        path: 'admin',
        canActivate: [RoleGuard], data: { roles: ['ADMIN'] },
        loadChildren: () => import('./features/admin/admin.module').then(m => m.AdminModule)
      },
      { path: 'chat', loadChildren: () => import('./features/chat/chat.module').then(m => m.ChatModule) },
      { path: '', redirectTo: 'products', pathMatch: 'full' },
    ]
  },
  { path: '**', redirectTo: '/auth/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
