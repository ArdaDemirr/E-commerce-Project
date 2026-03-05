# 🛍️ E-Commerce Analytics Platform — Frontend Agent Talimatları
## CSE 214 | Angular Frontend Implementation Guide

---

## ⚠️ KRİTİK KURALLAR — ÖNCE OKU

```
✅ SADECE frontend/ klasörü içinde çalış
✅ backend/ klasörünü sadece API kontratlarını anlamak için OKU
❌ backend/ klasöründe HİÇBİR dosya oluşturma, değiştirme veya silme
❌ database/, etl/, docs/ klasörlerine dokunma
❌ Kök dizinde yeni dosya oluşturma (docker-compose, .env hariç)
```

---

## 📁 Çalışma Dizini

```
ecommerce-platform/
├── backend/         ← SADECE OKU (API endpoint'leri için)
└── frontend/        ← TÜM İŞLER BURADA
```

---

## 🎨 Tasarım Felsefesi & Tema

### Renk Paleti
```
Primary:     #6366F1  (Indigo)
Secondary:   #8B5CF6  (Violet)
Accent:      #06B6D4  (Cyan)
Success:     #10B981  (Emerald)
Warning:     #F59E0B  (Amber)
Danger:      #EF4444  (Red)
Dark BG:     #0F172A  (Slate-900)
Card BG:     #1E293B  (Slate-800)
Border:      #334155  (Slate-700)
Text Main:   #F1F5F9  (Slate-100)
Text Muted:  #94A3B8  (Slate-400)
```

### Tasarım Dili
- **Dark theme** varsayılan, light mode toggle opsiyonel
- **Glassmorphism** kartlar: `backdrop-blur`, yarı saydam arka planlar
- **Gradient** başlıklar ve CTA butonlar
- **Micro-animations**: hover, focus, loading state'lerde
- **Smooth transitions**: sayfa geçişleri, dropdown'lar, modal'lar
- Referans UI: DataPulse (PDF'deki örnek ekranlar)

---

## 🛠️ Teknoloji Stack

```bash
# Angular Projesi Oluştur
ng new frontend --routing --style=scss --standalone=false

# Bağımlılıklar
npm install @angular/material @angular/cdk
npm install @ngrx/store @ngrx/effects @ngrx/entity @ngrx/router-store
npm install chart.js ng2-charts
npm install tailwindcss @tailwindcss/forms
npm install @tailwindcss/typography
npm install animate.css
npm install @angular/animations
npm install axios           # HTTP alternatifi (opsiyonel)
npm install lucide-angular  # İkonlar
npm install ngx-toastr      # Toast notifications
npm install ngx-spinner     # Loading spinner
npm install @types/chart.js
```

### Tailwind Kurulumu
```bash
npx tailwindcss init
```

`tailwind.config.js`:
```js
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primary:   { DEFAULT: '#6366F1', dark: '#4F46E5' },
        secondary: { DEFAULT: '#8B5CF6', dark: '#7C3AED' },
        accent:    { DEFAULT: '#06B6D4', dark: '#0891B2' },
        surface:   { DEFAULT: '#1E293B', light: '#334155' },
        background:{ DEFAULT: '#0F172A' },
      },
      animation: {
        'fade-in':    'fadeIn 0.3s ease-in-out',
        'slide-up':   'slideUp 0.4s ease-out',
        'slide-in':   'slideIn 0.3s ease-out',
        'pulse-slow': 'pulse 3s cubic-bezier(0.4,0,0.6,1) infinite',
        'glow':       'glow 2s ease-in-out infinite alternate',
      },
      keyframes: {
        fadeIn:  { '0%': { opacity: '0' }, '100%': { opacity: '1' } },
        slideUp: { '0%': { transform: 'translateY(20px)', opacity: '0' }, '100%': { transform: 'translateY(0)', opacity: '1' } },
        slideIn: { '0%': { transform: 'translateX(-20px)', opacity: '0' }, '100%': { transform: 'translateX(0)', opacity: '1' } },
        glow:    { '0%': { boxShadow: '0 0 5px #6366F1' }, '100%': { boxShadow: '0 0 20px #6366F1, 0 0 40px #8B5CF6' } },
      },
      backdropBlur: { xs: '2px' },
    }
  },
  plugins: [require('@tailwindcss/forms'), require('@tailwindcss/typography')]
}
```

`angular.json` → `styles` dizinine ekle:
```json
"styles": [
  "src/styles.scss",
  "node_modules/animate.css/animate.min.css",
  "node_modules/ngx-toastr/toastr.css"
]
```

---

## 📁 Frontend Klasör Yapısı (Tam)

```
frontend/
├── src/
│   ├── app/
│   │   ├── core/
│   │   │   ├── guards/
│   │   │   │   ├── auth.guard.ts
│   │   │   │   └── role.guard.ts
│   │   │   ├── interceptors/
│   │   │   │   └── jwt.interceptor.ts
│   │   │   ├── services/
│   │   │   │   ├── auth.service.ts
│   │   │   │   ├── api.service.ts
│   │   │   │   ├── token.service.ts
│   │   │   │   └── theme.service.ts
│   │   │   └── models/
│   │   │       ├── user.model.ts
│   │   │       ├── product.model.ts
│   │   │       ├── order.model.ts
│   │   │       ├── shipment.model.ts
│   │   │       ├── review.model.ts
│   │   │       ├── store.model.ts
│   │   │       ├── category.model.ts
│   │   │       └── analytics.model.ts
│   │   ├── shared/
│   │   │   ├── components/
│   │   │   │   ├── navbar/
│   │   │   │   ├── sidebar/
│   │   │   │   ├── kpi-card/
│   │   │   │   ├── data-table/
│   │   │   │   ├── modal/
│   │   │   │   ├── badge/
│   │   │   │   ├── loading-skeleton/
│   │   │   │   ├── empty-state/
│   │   │   │   └── confirm-dialog/
│   │   │   └── pipes/
│   │   │       ├── currency-format.pipe.ts
│   │   │       └── date-format.pipe.ts
│   │   ├── features/
│   │   │   ├── auth/
│   │   │   │   ├── login/
│   │   │   │   ├── register/
│   │   │   │   └── auth.module.ts
│   │   │   ├── dashboard/
│   │   │   │   ├── individual-dashboard/
│   │   │   │   ├── corporate-dashboard/
│   │   │   │   ├── admin-dashboard/
│   │   │   │   └── dashboard.module.ts
│   │   │   ├── products/
│   │   │   │   ├── product-list/
│   │   │   │   ├── product-detail/
│   │   │   │   ├── product-form/
│   │   │   │   └── products.module.ts
│   │   │   ├── orders/
│   │   │   │   ├── order-list/
│   │   │   │   ├── order-detail/
│   │   │   │   ├── checkout/
│   │   │   │   └── orders.module.ts
│   │   │   ├── cart/
│   │   │   │   ├── cart-page/
│   │   │   │   └── cart.module.ts
│   │   │   ├── shipments/
│   │   │   │   ├── shipment-list/
│   │   │   │   ├── shipment-tracking/
│   │   │   │   └── shipments.module.ts
│   │   │   ├── reviews/
│   │   │   │   ├── review-list/
│   │   │   │   ├── review-form/
│   │   │   │   └── reviews.module.ts
│   │   │   ├── analytics/
│   │   │   │   ├── sales-analytics/
│   │   │   │   ├── customer-analytics/
│   │   │   │   ├── product-analytics/
│   │   │   │   └── analytics.module.ts
│   │   │   ├── admin/
│   │   │   │   ├── user-management/
│   │   │   │   ├── store-management/
│   │   │   │   ├── category-management/
│   │   │   │   └── admin.module.ts
│   │   │   └── chat/
│   │   │       ├── chat-page/
│   │   │       └── chat.module.ts
│   │   ├── store/  (NgRx)
│   │   │   ├── auth/
│   │   │   │   ├── auth.actions.ts
│   │   │   │   ├── auth.reducer.ts
│   │   │   │   ├── auth.effects.ts
│   │   │   │   └── auth.selectors.ts
│   │   │   ├── cart/
│   │   │   │   ├── cart.actions.ts
│   │   │   │   ├── cart.reducer.ts
│   │   │   │   └── cart.selectors.ts
│   │   │   ├── products/
│   │   │   └── app.state.ts
│   │   ├── layout/
│   │   │   ├── main-layout/
│   │   │   │   ├── main-layout.component.ts
│   │   │   │   └── main-layout.component.html
│   │   │   └── auth-layout/
│   │   │       ├── auth-layout.component.ts
│   │   │       └── auth-layout.component.html
│   │   ├── app-routing.module.ts
│   │   ├── app.module.ts
│   │   └── app.component.ts
│   ├── assets/
│   │   ├── images/
│   │   └── icons/
│   ├── environments/
│   │   ├── environment.ts
│   │   └── environment.prod.ts
│   └── styles.scss
├── tailwind.config.js
├── angular.json
└── package.json
```

---

## ⚙️ Environment Konfigürasyonu

### `src/environments/environment.ts`
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  appName: 'DataPulse',
  version: '1.0.0'
};
```

---

## 🔐 FAZ 1 — Core Altyapı

### 1.1 TypeScript Model Interface'leri

`src/app/core/models/user.model.ts`:
```typescript
export type UserRole = 'ADMIN' | 'CORPORATE' | 'INDIVIDUAL';

export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  isActive: boolean;
  createdAt: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role: UserRole;
}
```

`src/app/core/models/product.model.ts`:
```typescript
export interface Product {
  id: number;
  storeId: number;
  categoryId: number;
  sku: string;
  name: string;
  description: string;
  unitPrice: number;
  stockQty: number;
  categoryName?: string;
  storeName?: string;
  avgRating?: number;
  createdAt: string;
}

export interface ProductFilter {
  search?: string;
  categoryId?: number;
  minPrice?: number;
  maxPrice?: number;
  sortBy?: 'price_asc' | 'price_desc' | 'rating' | 'newest';
  page?: number;
  size?: number;
}
```

`src/app/core/models/order.model.ts`:
```typescript
export type OrderStatus = 'PENDING' | 'SHIPPED' | 'COMPLETED' | 'CANCELLED';

export interface Order {
  id: number;
  userId: number;
  storeId: number;
  status: OrderStatus;
  grandTotal: number;
  paymentMethod: string;
  createdAt: string;
  items: OrderItem[];
}

export interface OrderItem {
  id: number;
  productId: number;
  productName: string;
  quantity: number;
  price: number;
}

export interface CartItem {
  product: Product;
  quantity: number;
}
```

`src/app/core/models/analytics.model.ts`:
```typescript
export interface KpiData {
  label: string;
  value: number | string;
  change: number;
  changeType: 'increase' | 'decrease';
  icon: string;
}

export interface SalesData {
  date: string;
  revenue: number;
  orders: number;
}

export interface CategorySales {
  category: string;
  revenue: number;
  percentage: number;
}
```

---

### 1.2 Token Service

`src/app/core/services/token.service.ts`:
```typescript
import { Injectable } from '@angular/core';

const ACCESS_KEY  = 'access_token';
const REFRESH_KEY = 'refresh_token';
const USER_KEY    = 'current_user';

@Injectable({ providedIn: 'root' })
export class TokenService {
  setTokens(access: string, refresh: string): void {
    localStorage.setItem(ACCESS_KEY, access);
    localStorage.setItem(REFRESH_KEY, refresh);
  }
  getAccessToken(): string | null  { return localStorage.getItem(ACCESS_KEY); }
  getRefreshToken(): string | null { return localStorage.getItem(REFRESH_KEY); }
  setUser(user: any): void { localStorage.setItem(USER_KEY, JSON.stringify(user)); }
  getUser(): any {
    const u = localStorage.getItem(USER_KEY);
    return u ? JSON.parse(u) : null;
  }
  clear(): void {
    localStorage.removeItem(ACCESS_KEY);
    localStorage.removeItem(REFRESH_KEY);
    localStorage.removeItem(USER_KEY);
  }
  isLoggedIn(): boolean { return !!this.getAccessToken(); }
}
```

---

### 1.3 Auth Service

`src/app/core/services/auth.service.ts`:
```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { TokenService } from './token.service';
import { AuthResponse, LoginRequest, RegisterRequest, User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = environment.apiUrl;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    private router: Router
  ) {
    const user = this.tokenService.getUser();
    if (user) this.currentUserSubject.next(user);
  }

  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, req).pipe(
      tap(res => {
        this.tokenService.setTokens(res.accessToken, res.refreshToken);
        this.tokenService.setUser(res.user);
        this.currentUserSubject.next(res.user);
      })
    );
  }

  register(req: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/register`, req).pipe(
      tap(res => {
        this.tokenService.setTokens(res.accessToken, res.refreshToken);
        this.tokenService.setUser(res.user);
        this.currentUserSubject.next(res.user);
      })
    );
  }

  logout(): void {
    this.http.post(`${this.apiUrl}/auth/logout`, {}).subscribe();
    this.tokenService.clear();
    this.currentUserSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  refreshToken(): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/refresh`, {
      refreshToken: this.tokenService.getRefreshToken()
    }).pipe(
      tap(res => {
        this.tokenService.setTokens(res.accessToken, res.refreshToken);
      })
    );
  }

  get currentUser(): User | null { return this.currentUserSubject.value; }
  get isLoggedIn(): boolean      { return this.tokenService.isLoggedIn(); }
  get userRole(): string         { return this.currentUser?.role || ''; }
  hasRole(role: string): boolean { return this.userRole === role; }
}
```

---

### 1.4 JWT Interceptor

`src/app/core/interceptors/jwt.interceptor.ts`:
```typescript
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, filter, switchMap, take } from 'rxjs/operators';
import { TokenService } from '../services/token.service';
import { AuthService } from '../services/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject = new BehaviorSubject<string | null>(null);

  constructor(private tokenService: TokenService, private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.tokenService.getAccessToken();
    const authReq = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

    return next.handle(authReq).pipe(
      catchError((err: HttpErrorResponse) => {
        if (err.status === 401 && !req.url.includes('/auth/')) {
          return this.handle401(authReq, next);
        }
        return throwError(() => err);
      })
    );
  }

  private handle401(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);
      return this.authService.refreshToken().pipe(
        switchMap(res => {
          this.isRefreshing = false;
          this.refreshTokenSubject.next(res.accessToken);
          return next.handle(req.clone({ setHeaders: { Authorization: `Bearer ${res.accessToken}` } }));
        }),
        catchError(err => {
          this.isRefreshing = false;
          this.authService.logout();
          return throwError(() => err);
        })
      );
    }
    return this.refreshTokenSubject.pipe(
      filter(t => t !== null), take(1),
      switchMap(t => next.handle(req.clone({ setHeaders: { Authorization: `Bearer ${t}` } })))
    );
  }
}
```

---

### 1.5 Guards

`src/app/core/guards/auth.guard.ts`:
```typescript
import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}
  canActivate(): boolean | UrlTree {
    return this.auth.isLoggedIn ? true : this.router.createUrlTree(['/auth/login']);
  }
}
```

`src/app/core/guards/role.guard.ts`:
```typescript
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}
  canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
    const requiredRoles: string[] = route.data['roles'] || [];
    if (requiredRoles.includes(this.auth.userRole)) return true;
    return this.router.createUrlTree(['/unauthorized']);
  }
}
```

---

### 1.6 Routing

`src/app/app-routing.module.ts`:
```typescript
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
    children: [
      { path: 'login', loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule) },
      { path: 'register', loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule) },
    ]
  },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule) },
      { path: 'products',  loadChildren: () => import('./features/products/products.module').then(m => m.ProductsModule) },
      { path: 'cart',      loadChildren: () => import('./features/cart/cart.module').then(m => m.CartModule) },
      { path: 'orders',    loadChildren: () => import('./features/orders/orders.module').then(m => m.OrdersModule) },
      { path: 'shipments', loadChildren: () => import('./features/shipments/shipments.module').then(m => m.ShipmentsModule) },
      { path: 'reviews',   loadChildren: () => import('./features/reviews/reviews.module').then(m => m.ReviewsModule) },
      { path: 'analytics', loadChildren: () => import('./features/analytics/analytics.module').then(m => m.AnalyticsModule) },
      {
        path: 'admin',
        canActivate: [RoleGuard], data: { roles: ['ADMIN'] },
        loadChildren: () => import('./features/admin/admin.module').then(m => m.AdminModule)
      },
      { path: 'chat', loadChildren: () => import('./features/chat/chat.module').then(m => m.ChatModule) },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ]
  },
  { path: '**', redirectTo: '/auth/login' }
];

@NgModule({ imports: [RouterModule.forRoot(routes)], exports: [RouterModule] })
export class AppRoutingModule {}
```

---

## 🎨 FAZ 2 — Global Stiller

### `src/styles.scss`
```scss
@import 'tailwindcss/base';
@import 'tailwindcss/components';
@import 'tailwindcss/utilities';

// Scrollbar
::-webkit-scrollbar { width: 6px; height: 6px; }
::-webkit-scrollbar-track { background: #1E293B; }
::-webkit-scrollbar-thumb { background: #6366F1; border-radius: 3px; }

// Glass Card
.glass-card {
  background: rgba(30, 41, 59, 0.7);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(99, 102, 241, 0.2);
  border-radius: 16px;
  transition: all 0.3s ease;
  &:hover { border-color: rgba(99, 102, 241, 0.5); box-shadow: 0 8px 32px rgba(99, 102, 241, 0.15); }
}

// Gradient Button
.btn-primary {
  @apply px-6 py-3 rounded-xl font-semibold text-white transition-all duration-300;
  background: linear-gradient(135deg, #6366F1, #8B5CF6);
  &:hover { transform: translateY(-2px); box-shadow: 0 10px 25px rgba(99, 102, 241, 0.4); }
  &:active { transform: translateY(0); }
}

.btn-secondary {
  @apply px-6 py-3 rounded-xl font-semibold transition-all duration-300;
  background: rgba(99, 102, 241, 0.15);
  border: 1px solid rgba(99, 102, 241, 0.3);
  color: #A5B4FC;
  &:hover { background: rgba(99, 102, 241, 0.25); }
}

// Gradient Text
.gradient-text {
  background: linear-gradient(135deg, #6366F1, #06B6D4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

// Status Badges
.badge {
  @apply px-3 py-1 rounded-full text-xs font-semibold;
  &.pending   { background: rgba(245,158,11,0.2); color: #FCD34D; }
  &.shipped   { background: rgba(6,182,212,0.2);  color: #67E8F9; }
  &.completed { background: rgba(16,185,129,0.2); color: #6EE7B7; }
  &.cancelled { background: rgba(239,68,68,0.2);  color: #FCA5A5; }
}

// Page transition
.page-enter { animation: fadeIn 0.3s ease-in-out; }

// KPI Card
.kpi-card {
  @apply glass-card p-6 cursor-default;
  position: relative;
  overflow: hidden;
  &::before {
    content: '';
    position: absolute;
    top: -50%; right: -50%;
    width: 200%; height: 200%;
    background: radial-gradient(circle, rgba(99,102,241,0.05) 0%, transparent 70%);
    pointer-events: none;
  }
}

// Sidebar item
.sidebar-item {
  @apply flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 cursor-pointer;
  color: #94A3B8;
  &:hover { background: rgba(99,102,241,0.1); color: #A5B4FC; }
  &.active { background: rgba(99,102,241,0.2); color: #6366F1; border-left: 3px solid #6366F1; }
}
```

---

## 🏠 FAZ 3 — Layout Bileşenleri

### 3.1 Main Layout

`src/app/layout/main-layout/main-layout.component.html`:
```html
<div class="flex h-screen bg-background overflow-hidden">
  <!-- Sidebar -->
  <app-sidebar
    [isCollapsed]="sidebarCollapsed"
    (toggleSidebar)="toggleSidebar()">
  </app-sidebar>

  <!-- Main Content -->
  <div class="flex-1 flex flex-col overflow-hidden transition-all duration-300"
       [class.ml-64]="!sidebarCollapsed"
       [class.ml-16]="sidebarCollapsed">

    <!-- Navbar -->
    <app-navbar
      (toggleSidebar)="toggleSidebar()"
      [sidebarCollapsed]="sidebarCollapsed">
    </app-navbar>

    <!-- Page Content -->
    <main class="flex-1 overflow-y-auto p-6 bg-background">
      <div class="animate__animated animate__fadeIn">
        <router-outlet></router-outlet>
      </div>
    </main>
  </div>
</div>
```

---

### 3.2 Navbar Bileşeni

`src/app/shared/components/navbar/navbar.component.html`:
```html
<header class="h-16 flex items-center justify-between px-6 border-b border-slate-700/50"
        style="background: rgba(15,23,42,0.8); backdrop-filter: blur(12px);">

  <!-- Left: Hamburger + Breadcrumb -->
  <div class="flex items-center gap-4">
    <button (click)="toggleSidebar.emit()"
            class="p-2 rounded-lg hover:bg-slate-700/50 transition-colors text-slate-400 hover:text-white">
      <lucide-icon name="menu" [size]="20"></lucide-icon>
    </button>
    <span class="text-slate-500 text-sm">{{ currentPageTitle }}</span>
  </div>

  <!-- Right: Search + Notifications + User -->
  <div class="flex items-center gap-3">

    <!-- Search -->
    <div class="relative hidden md:block">
      <input type="text" placeholder="Ara..."
             class="w-64 bg-slate-800/80 border border-slate-700 rounded-xl px-4 py-2 pl-10
                    text-sm text-slate-300 placeholder-slate-500
                    focus:outline-none focus:border-primary/60 focus:bg-slate-800 transition-all">
      <lucide-icon name="search" [size]="16" class="absolute left-3 top-2.5 text-slate-500"></lucide-icon>
    </div>

    <!-- Notifications -->
    <button class="relative p-2 rounded-xl hover:bg-slate-700/50 transition-colors text-slate-400 hover:text-white">
      <lucide-icon name="bell" [size]="20"></lucide-icon>
      <span class="absolute top-1 right-1 w-2 h-2 bg-primary rounded-full"></span>
    </button>

    <!-- User Menu -->
    <div class="relative" (click)="toggleUserMenu()">
      <div class="flex items-center gap-3 cursor-pointer px-3 py-2 rounded-xl hover:bg-slate-700/50 transition-colors">
        <div class="w-8 h-8 rounded-xl flex items-center justify-center font-bold text-sm text-white"
             style="background: linear-gradient(135deg, #6366F1, #8B5CF6);">
          {{ userInitials }}
        </div>
        <div class="hidden md:block">
          <p class="text-sm font-medium text-slate-200">{{ currentUser?.firstName }}</p>
          <p class="text-xs text-slate-500">{{ currentUser?.role }}</p>
        </div>
        <lucide-icon name="chevron-down" [size]="16" class="text-slate-500"></lucide-icon>
      </div>

      <!-- Dropdown -->
      <div *ngIf="showUserMenu"
           class="absolute right-0 top-12 w-48 glass-card py-2 z-50 animate__animated animate__fadeIn animate__faster">
        <a class="flex items-center gap-2 px-4 py-2 text-sm text-slate-300 hover:text-white hover:bg-slate-700/50 cursor-pointer">
          <lucide-icon name="user" [size]="16"></lucide-icon> Profil
        </a>
        <hr class="border-slate-700 my-1">
        <a (click)="logout()" class="flex items-center gap-2 px-4 py-2 text-sm text-red-400 hover:text-red-300 hover:bg-red-500/10 cursor-pointer">
          <lucide-icon name="log-out" [size]="16"></lucide-icon> Çıkış Yap
        </a>
      </div>
    </div>
  </div>
</header>
```

---

### 3.3 Sidebar Bileşeni

`src/app/shared/components/sidebar/sidebar.component.html`:
```html
<aside class="fixed left-0 top-0 h-full z-40 transition-all duration-300 flex flex-col border-r border-slate-700/50"
       [class.w-64]="!isCollapsed" [class.w-16]="isCollapsed"
       style="background: rgba(15,23,42,0.95); backdrop-filter: blur(20px);">

  <!-- Logo -->
  <div class="h-16 flex items-center px-4 border-b border-slate-700/50 gap-3">
    <div class="w-8 h-8 rounded-lg flex items-center justify-center shrink-0"
         style="background: linear-gradient(135deg, #6366F1, #8B5CF6);">
      <lucide-icon name="zap" [size]="16" class="text-white"></lucide-icon>
    </div>
    <span *ngIf="!isCollapsed" class="font-bold text-white text-lg gradient-text animate__animated animate__fadeIn">
      DataPulse
    </span>
  </div>

  <!-- Navigation -->
  <nav class="flex-1 py-4 overflow-y-auto">

    <!-- MAIN MENU -->
    <div class="px-3 mb-2">
      <p *ngIf="!isCollapsed" class="text-xs font-semibold text-slate-500 uppercase tracking-wider px-2 mb-2">
        Ana Menü
      </p>

      <a *ngFor="let item of mainMenuItems"
         [routerLink]="item.path" routerLinkActive="active"
         class="sidebar-item mb-1"
         [matTooltip]="isCollapsed ? item.label : ''" matTooltipPosition="right">
        <lucide-icon [name]="item.icon" [size]="20" class="shrink-0"></lucide-icon>
        <span *ngIf="!isCollapsed" class="animate__animated animate__fadeIn">{{ item.label }}</span>
        <span *ngIf="!isCollapsed && item.badge"
              class="ml-auto px-2 py-0.5 rounded-full text-xs font-bold"
              style="background: linear-gradient(135deg, #6366F1, #8B5CF6); color: white">
          {{ item.badge }}
        </span>
      </a>
    </div>

    <!-- MANAGEMENT (Corporate + Admin) -->
    <div *ngIf="showManagement" class="px-3 mt-4">
      <p *ngIf="!isCollapsed" class="text-xs font-semibold text-slate-500 uppercase tracking-wider px-2 mb-2">
        Yönetim
      </p>
      <a *ngFor="let item of managementItems"
         [routerLink]="item.path" routerLinkActive="active"
         class="sidebar-item mb-1">
        <lucide-icon [name]="item.icon" [size]="20" class="shrink-0"></lucide-icon>
        <span *ngIf="!isCollapsed">{{ item.label }}</span>
      </a>
    </div>
  </nav>

  <!-- Bottom: Theme Toggle -->
  <div class="p-3 border-t border-slate-700/50">
    <button class="sidebar-item w-full">
      <lucide-icon name="moon" [size]="20" class="shrink-0"></lucide-icon>
      <span *ngIf="!isCollapsed">Karanlık Mod</span>
    </button>
  </div>
</aside>
```

**`sidebar.component.ts`** — Rol bazlı menü mantığı:
```typescript
// mainMenuItems roleye göre filtrelenir:
// INDIVIDUAL: Dashboard, Ürünler, Sepet, Siparişlerim, Kargo Takibi, Yorumlarım, Analizlerim
// CORPORATE:  Dashboard, Ürünler, Siparişler, Müşteriler, Analizler, Yorumlar, Mağaza Ayarları
// ADMIN:      Dashboard, Kullanıcılar, Mağazalar, Kategoriler, Analizler, Audit Log
```

---

## 🔑 FAZ 4 — Auth Sayfaları

### Login Sayfası

`src/app/features/auth/login/login.component.html`:
```html
<div class="min-h-screen flex items-center justify-center p-4"
     style="background: radial-gradient(ellipse at top, #1e1b4b 0%, #0F172A 50%);">

  <!-- Animated Background Orbs -->
  <div class="fixed inset-0 overflow-hidden pointer-events-none">
    <div class="absolute -top-40 -right-40 w-96 h-96 rounded-full opacity-20 animate-pulse-slow"
         style="background: radial-gradient(circle, #6366F1, transparent)"></div>
    <div class="absolute -bottom-40 -left-40 w-96 h-96 rounded-full opacity-20 animate-pulse-slow"
         style="background: radial-gradient(circle, #8B5CF6, transparent); animation-delay: 1s;"></div>
  </div>

  <!-- Card -->
  <div class="w-full max-w-md glass-card p-8 animate__animated animate__slideInUp">

    <!-- Logo -->
    <div class="text-center mb-8">
      <div class="w-16 h-16 rounded-2xl mx-auto mb-4 flex items-center justify-center"
           style="background: linear-gradient(135deg, #6366F1, #8B5CF6);">
        <lucide-icon name="zap" [size]="28" class="text-white"></lucide-icon>
      </div>
      <h1 class="text-2xl font-bold gradient-text">DataPulse</h1>
      <p class="text-slate-400 text-sm mt-1">E-Commerce Analytics Platform</p>
    </div>

    <!-- Form -->
    <form [formGroup]="loginForm" (ngSubmit)="onLogin()">

      <!-- Email -->
      <div class="mb-4">
        <label class="block text-sm font-medium text-slate-300 mb-2">E-posta</label>
        <div class="relative">
          <input formControlName="email" type="email" placeholder="ornek@mail.com"
                 class="w-full bg-slate-800/80 border border-slate-700 rounded-xl px-4 py-3 pl-11
                        text-slate-200 placeholder-slate-500
                        focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary/30 transition-all">
          <lucide-icon name="mail" [size]="18" class="absolute left-3.5 top-3.5 text-slate-500"></lucide-icon>
        </div>
        <p *ngIf="loginForm.get('email')?.invalid && loginForm.get('email')?.touched"
           class="text-red-400 text-xs mt-1 animate__animated animate__fadeIn">
          Geçerli bir e-posta girin
        </p>
      </div>

      <!-- Password -->
      <div class="mb-6">
        <label class="block text-sm font-medium text-slate-300 mb-2">Şifre</label>
        <div class="relative">
          <input formControlName="password" [type]="showPassword ? 'text' : 'password'"
                 placeholder="••••••••"
                 class="w-full bg-slate-800/80 border border-slate-700 rounded-xl px-4 py-3 pl-11 pr-11
                        text-slate-200 placeholder-slate-500
                        focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary/30 transition-all">
          <lucide-icon name="lock" [size]="18" class="absolute left-3.5 top-3.5 text-slate-500"></lucide-icon>
          <button type="button" (click)="showPassword = !showPassword"
                  class="absolute right-3.5 top-3.5 text-slate-500 hover:text-slate-300 transition-colors">
            <lucide-icon [name]="showPassword ? 'eye-off' : 'eye'" [size]="18"></lucide-icon>
          </button>
        </div>
      </div>

      <!-- Error Alert -->
      <div *ngIf="errorMessage" class="mb-4 p-3 rounded-xl bg-red-500/10 border border-red-500/20 text-red-400 text-sm animate__animated animate__shakeX">
        {{ errorMessage }}
      </div>

      <!-- Submit -->
      <button type="submit" [disabled]="loginForm.invalid || isLoading"
              class="btn-primary w-full flex items-center justify-center gap-2">
        <lucide-icon *ngIf="!isLoading" name="log-in" [size]="18"></lucide-icon>
        <div *ngIf="isLoading" class="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin"></div>
        {{ isLoading ? 'Giriş yapılıyor...' : 'Giriş Yap' }}
      </button>
    </form>

    <!-- Register Link -->
    <p class="text-center mt-6 text-slate-400 text-sm">
      Hesabın yok mu?
      <a routerLink="/auth/register" class="text-primary hover:text-violet-400 font-medium ml-1 transition-colors">
        Kayıt Ol
      </a>
    </p>
  </div>
</div>
```

---

## 📊 FAZ 5 — Dashboard Sayfaları

### 5.1 KPI Card Shared Bileşen

`src/app/shared/components/kpi-card/kpi-card.component.html`:
```html
<div class="kpi-card animate__animated animate__fadeInUp" [style.animation-delay]="delay">
  <div class="flex items-start justify-between mb-4">
    <div class="w-12 h-12 rounded-xl flex items-center justify-center"
         [style.background]="'rgba(' + iconBg + ', 0.15)'">
      <lucide-icon [name]="icon" [size]="22" [style.color]="iconColor"></lucide-icon>
    </div>
    <span class="flex items-center gap-1 text-sm font-medium px-2 py-1 rounded-lg"
          [class.text-emerald-400]="change >= 0"
          [class.text-red-400]="change < 0"
          [class.bg-emerald-400/10]="change >= 0"
          [class.bg-red-400/10]="change < 0">
      <lucide-icon [name]="change >= 0 ? 'trending-up' : 'trending-down'" [size]="14"></lucide-icon>
      {{ change >= 0 ? '+' : '' }}{{ change }}%
    </span>
  </div>
  <p class="text-3xl font-bold text-white mb-1">{{ value }}</p>
  <p class="text-slate-400 text-sm">{{ label }}</p>
</div>
```

### 5.2 Corporate Dashboard

`src/app/features/dashboard/corporate-dashboard/corporate-dashboard.component.html`:
```html
<div class="space-y-6">

  <!-- Header -->
  <div class="flex items-center justify-between">
    <div>
      <h1 class="text-2xl font-bold text-white">
        Hoş geldin, <span class="gradient-text">{{ user?.firstName }}</span> 👋
      </h1>
      <p class="text-slate-400 text-sm mt-1">Mağazanda bugün neler oluyor</p>
    </div>
    <div class="flex gap-3">
      <button class="btn-secondary flex items-center gap-2 text-sm">
        <lucide-icon name="download" [size]="16"></lucide-icon> Rapor İndir
      </button>
      <button class="btn-primary flex items-center gap-2 text-sm">
        <lucide-icon name="plus" [size]="16"></lucide-icon> Ürün Ekle
      </button>
    </div>
  </div>

  <!-- KPI Cards -->
  <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-4">
    <app-kpi-card
      *ngFor="let kpi of kpis; let i = index"
      [label]="kpi.label"
      [value]="kpi.value"
      [change]="kpi.change"
      [icon]="kpi.icon"
      [iconColor]="kpi.iconColor"
      [iconBg]="kpi.iconBg"
      [delay]="(i * 0.1) + 's'">
    </app-kpi-card>
  </div>

  <!-- Charts Row -->
  <div class="grid grid-cols-1 xl:grid-cols-3 gap-6">

    <!-- Revenue Chart (2/3 width) -->
    <div class="xl:col-span-2 glass-card p-6">
      <div class="flex items-center justify-between mb-6">
        <h3 class="font-semibold text-white">Gelir Trendi</h3>
        <div class="flex gap-2">
          <button *ngFor="let p of ['7G', '30G', '90G', '1Y']"
                  (click)="setRevenuePeriod(p)"
                  class="px-3 py-1 rounded-lg text-xs font-medium transition-all"
                  [class.bg-primary]="revenuePeriod === p"
                  [class.text-white]="revenuePeriod === p"
                  [class.text-slate-400]="revenuePeriod !== p"
                  [class.hover:text-white]="revenuePeriod !== p">
            {{ p }}
          </button>
        </div>
      </div>
      <canvas baseChart
              [data]="revenueChartData"
              [options]="revenueChartOptions"
              [type]="'line'"
              style="max-height: 280px;">
      </canvas>
    </div>

    <!-- Categories Pie (1/3 width) -->
    <div class="glass-card p-6">
      <h3 class="font-semibold text-white mb-6">Kategoriler</h3>
      <canvas baseChart
              [data]="categoryChartData"
              [options]="categoryChartOptions"
              [type]="'doughnut'"
              style="max-height: 200px;">
      </canvas>
      <div class="mt-4 space-y-2">
        <div *ngFor="let cat of categories" class="flex items-center justify-between text-sm">
          <span class="flex items-center gap-2 text-slate-400">
            <span class="w-2.5 h-2.5 rounded-full" [style.background]="cat.color"></span>
            {{ cat.name }}
          </span>
          <span class="text-slate-300 font-medium">{{ cat.percentage }}%</span>
        </div>
      </div>
    </div>
  </div>

  <!-- Recent Orders Table -->
  <div class="glass-card p-6">
    <div class="flex items-center justify-between mb-6">
      <h3 class="font-semibold text-white">Son Siparişler</h3>
      <a routerLink="/orders" class="text-primary text-sm hover:text-violet-400 transition-colors">
        Tümünü Gör →
      </a>
    </div>
    <app-data-table
      [columns]="orderColumns"
      [data]="recentOrders"
      [loading]="loadingOrders">
    </app-data-table>
  </div>
</div>
```

**Chart.js Config (component.ts içinde):**
```typescript
revenueChartData: ChartData<'line'> = {
  labels: [],
  datasets: [{
    label: 'Gelir',
    data: [],
    borderColor: '#6366F1',
    backgroundColor: 'rgba(99,102,241,0.1)',
    borderWidth: 2,
    fill: true,
    tension: 0.4,
    pointBackgroundColor: '#6366F1',
    pointRadius: 4,
    pointHoverRadius: 6,
  }]
};

revenueChartOptions: ChartOptions<'line'> = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { display: false },
    tooltip: {
      backgroundColor: '#1E293B',
      borderColor: '#6366F1',
      borderWidth: 1,
      titleColor: '#F1F5F9',
      bodyColor: '#94A3B8',
    }
  },
  scales: {
    x: { grid: { color: 'rgba(51,65,85,0.5)' }, ticks: { color: '#64748B' } },
    y: { grid: { color: 'rgba(51,65,85,0.5)' }, ticks: { color: '#64748B' } }
  }
};
```

---

## 🛍️ FAZ 6 — Ürün Modülü

### Product List (Individual için katalog)

`src/app/features/products/product-list/product-list.component.html`:
```html
<div class="space-y-6">

  <!-- Header + Filters -->
  <div class="glass-card p-4">
    <div class="flex flex-col md:flex-row gap-4 items-start md:items-center">
      <!-- Search -->
      <div class="relative flex-1">
        <input [(ngModel)]="filters.search" (ngModelChange)="onSearch()"
               placeholder="Ürün, SKU veya kategori ara..."
               class="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-2.5 pl-10
                      text-sm text-slate-200 placeholder-slate-500 focus:outline-none focus:border-primary/60 transition-all">
        <lucide-icon name="search" [size]="16" class="absolute left-3.5 top-3 text-slate-500"></lucide-icon>
      </div>

      <!-- Category Filter -->
      <select [(ngModel)]="filters.categoryId" (ngModelChange)="loadProducts()"
              class="bg-slate-800 border border-slate-700 rounded-xl px-4 py-2.5 text-sm text-slate-300 focus:outline-none focus:border-primary/60">
        <option [value]="null">Tüm Kategoriler</option>
        <option *ngFor="let cat of categories" [value]="cat.id">{{ cat.name }}</option>
      </select>

      <!-- Sort -->
      <select [(ngModel)]="filters.sortBy" (ngModelChange)="loadProducts()"
              class="bg-slate-800 border border-slate-700 rounded-xl px-4 py-2.5 text-sm text-slate-300 focus:outline-none focus:border-primary/60">
        <option value="newest">En Yeni</option>
        <option value="price_asc">Fiyat: Düşük → Yüksek</option>
        <option value="price_desc">Fiyat: Yüksek → Düşük</option>
        <option value="rating">En Çok Değerlendirilen</option>
      </select>

      <!-- View Toggle -->
      <div class="flex gap-1 bg-slate-800 rounded-xl p-1 border border-slate-700">
        <button (click)="viewMode = 'grid'" class="p-2 rounded-lg transition-all"
                [class.bg-primary]="viewMode === 'grid'" [class.text-white]="viewMode === 'grid'"
                [class.text-slate-500]="viewMode !== 'grid'">
          <lucide-icon name="grid" [size]="16"></lucide-icon>
        </button>
        <button (click)="viewMode = 'list'" class="p-2 rounded-lg transition-all"
                [class.bg-primary]="viewMode === 'list'" [class.text-white]="viewMode === 'list'"
                [class.text-slate-500]="viewMode !== 'list'">
          <lucide-icon name="list" [size]="16"></lucide-icon>
        </button>
      </div>
    </div>
  </div>

  <!-- Loading Skeleton -->
  <div *ngIf="loading" class="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-4">
    <div *ngFor="let i of [1,2,3,4,5,6,7,8]"
         class="glass-card p-4 animate-pulse">
      <div class="w-full h-40 bg-slate-700/50 rounded-xl mb-4"></div>
      <div class="h-4 bg-slate-700/50 rounded mb-2 w-3/4"></div>
      <div class="h-3 bg-slate-700/50 rounded w-1/2"></div>
    </div>
  </div>

  <!-- Product Grid -->
  <div *ngIf="!loading" class="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-4">
    <div *ngFor="let product of products; let i = index"
         class="glass-card overflow-hidden group cursor-pointer animate__animated animate__fadeInUp"
         [style.animation-delay]="(i * 0.05) + 's'"
         (click)="viewProduct(product.id)">

      <!-- Image Area -->
      <div class="relative h-44 overflow-hidden bg-slate-700/30">
        <div class="w-full h-full flex items-center justify-center text-slate-600">
          <lucide-icon name="package" [size]="48"></lucide-icon>
        </div>
        <!-- Hover Overlay -->
        <div class="absolute inset-0 bg-primary/20 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center gap-2">
          <button (click)="addToCart(product, $event)"
                  class="p-2 rounded-xl bg-white/10 backdrop-blur-sm hover:bg-white/20 text-white transition-all transform translate-y-4 group-hover:translate-y-0">
            <lucide-icon name="shopping-cart" [size]="18"></lucide-icon>
          </button>
        </div>
        <!-- Category Badge -->
        <span class="absolute top-2 left-2 px-2 py-0.5 rounded-lg text-xs bg-slate-900/80 text-slate-300 backdrop-blur-sm">
          {{ product.categoryName }}
        </span>
        <!-- Low Stock -->
        <span *ngIf="product.stockQty < 10" class="absolute top-2 right-2 px-2 py-0.5 rounded-lg text-xs bg-amber-500/20 text-amber-400 border border-amber-500/30">
          Son {{ product.stockQty }}
        </span>
      </div>

      <!-- Info -->
      <div class="p-4">
        <h3 class="font-medium text-slate-200 text-sm mb-1 truncate group-hover:text-white transition-colors">
          {{ product.name }}
        </h3>
        <p class="text-xs text-slate-500 mb-3">SKU: {{ product.sku }}</p>

        <!-- Rating -->
        <div class="flex items-center gap-1 mb-3" *ngIf="product.avgRating">
          <lucide-icon name="star" [size]="12" class="text-amber-400 fill-amber-400"></lucide-icon>
          <span class="text-xs text-slate-400">{{ product.avgRating | number:'1.1-1' }}</span>
        </div>

        <!-- Price + Cart -->
        <div class="flex items-center justify-between">
          <span class="text-lg font-bold text-white">${{ product.unitPrice | number:'1.2-2' }}</span>
          <button (click)="addToCart(product, $event)"
                  class="p-2 rounded-xl transition-all"
                  style="background: rgba(99,102,241,0.15); color: #A5B4FC;"
                  onmouseover="this.style.background='rgba(99,102,241,0.3)'"
                  onmouseout="this.style.background='rgba(99,102,241,0.15)'">
            <lucide-icon name="plus" [size]="16"></lucide-icon>
          </button>
        </div>
      </div>
    </div>
  </div>

  <!-- Pagination -->
  <div class="flex items-center justify-between mt-4">
    <p class="text-slate-400 text-sm">{{ totalProducts }} ürün</p>
    <div class="flex gap-2">
      <button (click)="prevPage()" [disabled]="currentPage === 0"
              class="px-4 py-2 rounded-xl text-sm bg-slate-800 border border-slate-700 text-slate-400
                     hover:border-primary/60 hover:text-white disabled:opacity-40 transition-all">
        ← Önceki
      </button>
      <span class="px-4 py-2 rounded-xl text-sm bg-primary/20 text-primary font-medium border border-primary/30">
        {{ currentPage + 1 }}
      </span>
      <button (click)="nextPage()" [disabled]="isLastPage"
              class="px-4 py-2 rounded-xl text-sm bg-slate-800 border border-slate-700 text-slate-400
                     hover:border-primary/60 hover:text-white disabled:opacity-40 transition-all">
        Sonraki →
      </button>
    </div>
  </div>
</div>
```

---

## 🛒 FAZ 7 — Sepet & Checkout

### Cart (NgRx State ile)

`src/app/store/cart/cart.actions.ts`:
```typescript
import { createAction, props } from '@ngrx/store';
import { CartItem, Product } from '../../core/models';

export const addToCart     = createAction('[Cart] Add',    props<{ product: Product; quantity?: number }>());
export const removeFromCart= createAction('[Cart] Remove', props<{ productId: number }>());
export const updateQty     = createAction('[Cart] Update', props<{ productId: number; quantity: number }>());
export const clearCart     = createAction('[Cart] Clear');
```

`src/app/features/cart/cart-page/cart-page.component.html`:
```html
<div class="max-w-4xl mx-auto space-y-6">
  <h1 class="text-2xl font-bold text-white">Sepetim
    <span class="text-slate-400 text-lg font-normal ml-2">({{ cartCount }} ürün)</span>
  </h1>

  <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

    <!-- Items -->
    <div class="lg:col-span-2 space-y-3">
      <div *ngIf="cartItems.length === 0" class="glass-card p-12 text-center">
        <lucide-icon name="shopping-cart" [size]="48" class="text-slate-600 mx-auto mb-4"></lucide-icon>
        <p class="text-slate-400">Sepetiniz boş</p>
        <a routerLink="/products" class="btn-primary inline-block mt-4">Alışverişe Başla</a>
      </div>

      <div *ngFor="let item of cartItems" class="glass-card p-4 flex items-center gap-4 animate__animated animate__fadeIn">
        <div class="w-16 h-16 rounded-xl bg-slate-700/50 flex items-center justify-center shrink-0">
          <lucide-icon name="package" [size]="28" class="text-slate-500"></lucide-icon>
        </div>
        <div class="flex-1 min-w-0">
          <h4 class="font-medium text-slate-200 truncate">{{ item.product.name }}</h4>
          <p class="text-sm text-slate-500">{{ item.product.categoryName }}</p>
          <p class="text-primary font-semibold mt-1">${{ item.product.unitPrice | number:'1.2-2' }}</p>
        </div>

        <!-- Quantity Controls -->
        <div class="flex items-center gap-2 bg-slate-800 rounded-xl p-1 border border-slate-700">
          <button (click)="decreaseQty(item)" class="w-8 h-8 rounded-lg hover:bg-slate-700 text-slate-400 hover:text-white transition-all flex items-center justify-center">-</button>
          <span class="w-8 text-center text-slate-200 font-medium text-sm">{{ item.quantity }}</span>
          <button (click)="increaseQty(item)" class="w-8 h-8 rounded-lg hover:bg-slate-700 text-slate-400 hover:text-white transition-all flex items-center justify-center">+</button>
        </div>

        <span class="text-white font-bold w-20 text-right">${{ (item.product.unitPrice * item.quantity) | number:'1.2-2' }}</span>

        <button (click)="removeItem(item)" class="p-2 rounded-xl text-slate-500 hover:text-red-400 hover:bg-red-500/10 transition-all">
          <lucide-icon name="trash-2" [size]="16"></lucide-icon>
        </button>
      </div>
    </div>

    <!-- Order Summary -->
    <div class="glass-card p-6 h-fit sticky top-6">
      <h3 class="font-semibold text-white mb-4">Sipariş Özeti</h3>
      <div class="space-y-3 mb-4">
        <div class="flex justify-between text-sm">
          <span class="text-slate-400">Ara Toplam</span>
          <span class="text-slate-200">${{ subtotal | number:'1.2-2' }}</span>
        </div>
        <div class="flex justify-between text-sm">
          <span class="text-slate-400">Kargo</span>
          <span class="text-emerald-400">Ücretsiz</span>
        </div>
        <hr class="border-slate-700">
        <div class="flex justify-between font-bold">
          <span class="text-white">Toplam</span>
          <span class="text-xl gradient-text">${{ subtotal | number:'1.2-2' }}</span>
        </div>
      </div>

      <!-- Payment Method -->
      <div class="mb-4">
        <label class="block text-sm text-slate-400 mb-2">Ödeme Yöntemi</label>
        <select [(ngModel)]="paymentMethod"
                class="w-full bg-slate-800 border border-slate-700 rounded-xl px-3 py-2 text-sm text-slate-300 focus:outline-none focus:border-primary/60">
          <option value="CREDIT_CARD">Kredi Kartı</option>
          <option value="DEBIT_CARD">Banka Kartı</option>
          <option value="BANK_TRANSFER">Havale/EFT</option>
        </select>
      </div>

      <button (click)="checkout()" [disabled]="cartItems.length === 0 || isProcessing"
              class="btn-primary w-full flex items-center justify-center gap-2">
        <div *ngIf="isProcessing" class="w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin"></div>
        <lucide-icon *ngIf="!isProcessing" name="credit-card" [size]="16"></lucide-icon>
        {{ isProcessing ? 'İşleniyor...' : 'Siparişi Tamamla' }}
      </button>
    </div>
  </div>
</div>
```

---

## 📈 FAZ 8 — Analytics Modülü

### Sales Analytics Sayfası

```html
<div class="space-y-6">
  <div class="flex items-center justify-between">
    <h1 class="text-2xl font-bold text-white">Satış Analitikleri 📊</h1>
    <div class="flex gap-3">
      <!-- Date Range -->
      <input type="date" [(ngModel)]="dateRange.start"
             class="bg-slate-800 border border-slate-700 rounded-xl px-3 py-2 text-sm text-slate-300 focus:outline-none focus:border-primary/60">
      <span class="text-slate-500 self-center">—</span>
      <input type="date" [(ngModel)]="dateRange.end"
             class="bg-slate-800 border border-slate-700 rounded-xl px-3 py-2 text-sm text-slate-300 focus:outline-none focus:border-primary/60">
      <button (click)="loadAnalytics()" class="btn-primary text-sm px-4">Uygula</button>
      <button (click)="exportCSV()" class="btn-secondary text-sm px-4 flex items-center gap-1">
        <lucide-icon name="download" [size]="14"></lucide-icon> CSV
      </button>
    </div>
  </div>

  <!-- 4 KPI Cards -->
  <div class="grid grid-cols-2 xl:grid-cols-4 gap-4">
    <app-kpi-card *ngFor="let k of analyticsKpis; let i = index"
                  [label]="k.label" [value]="k.value" [change]="k.change"
                  [icon]="k.icon" [delay]="(i*0.1)+'s'">
    </app-kpi-card>
  </div>

  <!-- Charts 2x2 Grid -->
  <div class="grid grid-cols-1 xl:grid-cols-2 gap-6">
    <!-- Revenue Line Chart -->
    <div class="glass-card p-6">
      <h3 class="font-semibold text-white mb-4">Aylık Gelir Trendi</h3>
      <canvas baseChart [data]="monthlyRevenue" [options]="lineOptions" type="line" style="max-height:260px"></canvas>
    </div>

    <!-- Order Status Bar Chart -->
    <div class="glass-card p-6">
      <h3 class="font-semibold text-white mb-4">Sipariş Durumu Dağılımı</h3>
      <canvas baseChart [data]="orderStatus" [options]="barOptions" type="bar" style="max-height:260px"></canvas>
    </div>

    <!-- Category Doughnut -->
    <div class="glass-card p-6">
      <h3 class="font-semibold text-white mb-4">Kategori Bazlı Gelir</h3>
      <canvas baseChart [data]="categoryRevenue" [options]="doughnutOptions" type="doughnut" style="max-height:260px"></canvas>
    </div>

    <!-- Top Products Horizontal Bar -->
    <div class="glass-card p-6">
      <h3 class="font-semibold text-white mb-4">En Çok Satan 5 Ürün</h3>
      <canvas baseChart [data]="topProducts" [options]="hBarOptions" type="bar" style="max-height:260px"></canvas>
    </div>
  </div>
</div>
```

---

## 👑 FAZ 9 — Admin Modülü

### User Management

```html
<div class="space-y-6">
  <div class="flex items-center justify-between">
    <h1 class="text-2xl font-bold text-white">Kullanıcı Yönetimi</h1>
    <div class="glass-card px-4 py-2 text-sm text-slate-400">
      Toplam: <span class="text-white font-bold">{{ totalUsers }}</span> kullanıcı
    </div>
  </div>

  <!-- Filters -->
  <div class="glass-card p-4 flex flex-wrap gap-3">
    <div class="relative flex-1 min-w-48">
      <input [(ngModel)]="search" placeholder="Ad, e-posta veya ID ara..."
             class="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-2 pl-10 text-sm text-slate-200 placeholder-slate-500 focus:outline-none focus:border-primary/60">
      <lucide-icon name="search" [size]="16" class="absolute left-3 top-2.5 text-slate-500"></lucide-icon>
    </div>
    <select [(ngModel)]="roleFilter" class="bg-slate-800 border border-slate-700 rounded-xl px-3 py-2 text-sm text-slate-300 focus:outline-none focus:border-primary/60">
      <option value="">Tüm Roller</option>
      <option value="ADMIN">Admin</option>
      <option value="CORPORATE">Corporate</option>
      <option value="INDIVIDUAL">Individual</option>
    </select>
    <select [(ngModel)]="statusFilter" class="bg-slate-800 border border-slate-700 rounded-xl px-3 py-2 text-sm text-slate-300 focus:outline-none focus:border-primary/60">
      <option value="">Tüm Durumlar</option>
      <option value="true">Aktif</option>
      <option value="false">Askıya Alınmış</option>
    </select>
  </div>

  <!-- Table -->
  <div class="glass-card overflow-hidden">
    <table class="w-full">
      <thead>
        <tr class="border-b border-slate-700/50">
          <th class="text-left px-6 py-4 text-xs font-semibold text-slate-500 uppercase">Kullanıcı</th>
          <th class="text-left px-6 py-4 text-xs font-semibold text-slate-500 uppercase">Rol</th>
          <th class="text-left px-6 py-4 text-xs font-semibold text-slate-500 uppercase">Kayıt Tarihi</th>
          <th class="text-left px-6 py-4 text-xs font-semibold text-slate-500 uppercase">Durum</th>
          <th class="text-right px-6 py-4 text-xs font-semibold text-slate-500 uppercase">İşlemler</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let user of users"
            class="border-b border-slate-700/30 hover:bg-slate-700/20 transition-colors">
          <td class="px-6 py-4">
            <div class="flex items-center gap-3">
              <div class="w-9 h-9 rounded-xl flex items-center justify-center text-sm font-bold text-white shrink-0"
                   style="background: linear-gradient(135deg, #6366F1, #8B5CF6);">
                {{ user.firstName[0] }}{{ user.lastName[0] }}
              </div>
              <div>
                <p class="text-sm font-medium text-slate-200">{{ user.firstName }} {{ user.lastName }}</p>
                <p class="text-xs text-slate-500">{{ user.email }}</p>
              </div>
            </div>
          </td>
          <td class="px-6 py-4">
            <span class="px-2.5 py-1 rounded-lg text-xs font-medium"
                  [class.bg-violet-500/20]="user.role === 'ADMIN'"     [class.text-violet-300]="user.role === 'ADMIN'"
                  [class.bg-cyan-500/20]="user.role === 'CORPORATE'"   [class.text-cyan-300]="user.role === 'CORPORATE'"
                  [class.bg-emerald-500/20]="user.role === 'INDIVIDUAL'" [class.text-emerald-300]="user.role === 'INDIVIDUAL'">
              {{ user.role }}
            </span>
          </td>
          <td class="px-6 py-4 text-sm text-slate-400">{{ user.createdAt | date:'dd MMM yyyy' }}</td>
          <td class="px-6 py-4">
            <span class="flex items-center gap-1.5 text-xs font-medium"
                  [class.text-emerald-400]="user.isActive" [class.text-red-400]="!user.isActive">
              <span class="w-1.5 h-1.5 rounded-full"
                    [class.bg-emerald-400]="user.isActive" [class.bg-red-400]="!user.isActive"></span>
              {{ user.isActive ? 'Aktif' : 'Askıda' }}
            </span>
          </td>
          <td class="px-6 py-4">
            <div class="flex items-center justify-end gap-2">
              <button (click)="toggleUserStatus(user)"
                      class="p-2 rounded-lg transition-all"
                      [class.hover:bg-red-500/10]="user.isActive" [class.text-red-400]="user.isActive"
                      [class.hover:bg-emerald-500/10]="!user.isActive" [class.text-emerald-400]="!user.isActive">
                <lucide-icon [name]="user.isActive ? 'user-x' : 'user-check'" [size]="16"></lucide-icon>
              </button>
              <button (click)="deleteUser(user)" class="p-2 rounded-lg text-slate-500 hover:text-red-400 hover:bg-red-500/10 transition-all">
                <lucide-icon name="trash-2" [size]="16"></lucide-icon>
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
```

---

## 🚢 FAZ 10 — Kargo Takibi & Yorumlar

### Shipment Tracking

```html
<!-- Kargo timeline bileşeni -->
<div class="glass-card p-6">
  <h3 class="font-semibold text-white mb-6">Kargo Durumu — #{{ shipment?.trackingNo }}</h3>

  <div class="relative">
    <!-- Vertical Line -->
    <div class="absolute left-5 top-0 bottom-0 w-0.5 bg-slate-700"></div>

    <div *ngFor="let step of trackingSteps; let last = last" class="relative flex gap-4 mb-6">
      <!-- Step Dot -->
      <div class="w-10 h-10 rounded-full flex items-center justify-center shrink-0 z-10 border-2"
           [class.border-primary]="step.completed"
           [class.bg-primary]="step.completed"
           [class.border-slate-600]="!step.completed"
           [class.bg-slate-800]="!step.completed">
        <lucide-icon [name]="step.icon" [size]="16"
                     [class.text-white]="step.completed"
                     [class.text-slate-600]="!step.completed">
        </lucide-icon>
      </div>
      <!-- Step Info -->
      <div class="pt-1.5" [class.opacity-40]="!step.completed">
        <p class="text-sm font-medium" [class.text-white]="step.completed" [class.text-slate-500]="!step.completed">
          {{ step.label }}
        </p>
        <p *ngIf="step.date" class="text-xs text-slate-500 mt-0.5">{{ step.date }}</p>
      </div>
    </div>
  </div>
</div>
```

---

## ⭐ Review Form

```html
<div class="glass-card p-6 max-w-xl">
  <h3 class="font-semibold text-white mb-6">Yorum Yaz</h3>

  <!-- Star Rating -->
  <div class="mb-4">
    <label class="block text-sm text-slate-400 mb-2">Puanınız</label>
    <div class="flex gap-2">
      <button *ngFor="let star of [1,2,3,4,5]"
              (click)="rating = star" (mouseenter)="hoverRating = star" (mouseleave)="hoverRating = 0"
              class="transition-transform hover:scale-110">
        <lucide-icon name="star" [size]="28"
                     class="transition-colors"
                     [class.text-amber-400]="star <= (hoverRating || rating)"
                     [class.fill-amber-400]="star <= (hoverRating || rating)"
                     [class.text-slate-600]="star > (hoverRating || rating)">
        </lucide-icon>
      </button>
    </div>
  </div>

  <!-- Review Text -->
  <div class="mb-4">
    <label class="block text-sm text-slate-400 mb-2">Yorumunuz</label>
    <textarea [(ngModel)]="reviewText" rows="4" placeholder="Ürün hakkındaki deneyiminizi paylaşın..."
              class="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-3 text-sm text-slate-200
                     placeholder-slate-500 resize-none focus:outline-none focus:border-primary/60 transition-all">
    </textarea>
  </div>

  <button (click)="submitReview()" class="btn-primary flex items-center gap-2">
    <lucide-icon name="send" [size]="16"></lucide-icon> Yorum Gönder
  </button>
</div>
```

---

## 🔌 FAZ 11 — API Service (Backend Bağlantısı)

`src/app/core/services/api.service.ts`:
```typescript
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private base = environment.apiUrl;

  constructor(private http: HttpClient) {}

  get<T>(path: string, params?: any): Observable<T> {
    const httpParams = params ? new HttpParams({ fromObject: params }) : undefined;
    return this.http.get<T>(`${this.base}${path}`, { params: httpParams });
  }
  post<T>(path: string, body: any): Observable<T> {
    return this.http.post<T>(`${this.base}${path}`, body);
  }
  put<T>(path: string, body: any): Observable<T> {
    return this.http.put<T>(`${this.base}${path}`, body);
  }
  delete<T>(path: string): Observable<T> {
    return this.http.delete<T>(`${this.base}${path}`);
  }
}
```

**Örnek servislerin backend endpoint eşleşmeleri:**
```
ProductService.getAll()           → GET  /api/products
ProductService.getById(id)        → GET  /api/products/:id
ProductService.create(dto)        → POST /api/products         (CORPORATE)
ProductService.update(id, dto)    → PUT  /api/products/:id     (CORPORATE)
ProductService.delete(id)         → DELETE /api/products/:id   (CORPORATE)

OrderService.create(dto)          → POST /api/orders           (INDIVIDUAL)
OrderService.getMyOrders()        → GET  /api/orders
OrderService.updateStatus(id,s)   → PUT  /api/orders/:id/status

AnalyticsService.getSales()       → GET  /api/analytics/sales
AnalyticsService.getTopProducts() → GET  /api/analytics/products/top

AdminService.getUsers()           → GET  /api/admin/users
AdminService.toggleUser(id)       → PUT  /api/admin/users/:id
AdminService.deleteUser(id)       → DELETE /api/admin/users/:id
```

---

## 📱 FAZ 12 — Responsive & Mobile

### Mobile Sidebar Overlay
```html
<!-- Mobilde sidebar açıkken arka plan overlay -->
<div *ngIf="isMobile && !sidebarCollapsed"
     class="fixed inset-0 bg-black/60 z-30 backdrop-blur-sm"
     (click)="closeSidebar()">
</div>
```

### Breakpoint Stratejisi
```scss
// Tailwind breakpoints kullan:
// sm: 640px   → mobil landscape
// md: 768px   → tablet
// lg: 1024px  → laptop
// xl: 1280px  → desktop
// 2xl: 1536px → wide

// Grid örnekleri:
// grid-cols-1 md:grid-cols-2 xl:grid-cols-4  → KPI kartları
// grid-cols-2 md:grid-cols-3 xl:grid-cols-4  → Ürün grid'i
// col-span-1 xl:col-span-2                   → Geniş chart
```

---

## ✅ Final Kontrol Listesi

### Fonksiyonellik
- [ ] Login / Register / Logout çalışıyor
- [ ] Role-based routing çalışıyor (Admin, Corporate, Individual)
- [ ] JWT interceptor token ekliyor, 401'de refresh deniyor
- [ ] Ürün kataloğu: arama, filtre, sıralama, sayfalama
- [ ] Sepet: ekle/çıkar, adet güncelle, toplam hesapla
- [ ] Sipariş oluşturma ve geçmiş görüntüleme
- [ ] Kargo takibi timeline
- [ ] Yorum yazma ve yıldız rating
- [ ] Corporate: ürün CRUD, sipariş durum güncelleme
- [ ] Admin: kullanıcı yönetimi, mağaza onaylama
- [ ] Analytics: 4 farklı Chart.js grafiği, tarih filtreli
- [ ] Toast notification'lar (başarı/hata)

### UI/UX
- [ ] Dark theme tutarlı (tüm sayfalarda)
- [ ] Glassmorphism kartlar gözüküyor
- [ ] Hover animasyonları çalışıyor
- [ ] Loading skeleton'lar veri yüklenirken görünüyor
- [ ] Empty state'ler boş listeler için gösteriliyor
- [ ] Mobile responsive — hamburger menü çalışıyor
- [ ] Gradient butonlar ve başlıklar doğru

### Kod Kalitesi
- [ ] Tüm HTTP çağrıları service'ler üzerinden
- [ ] Reaktif formlar validasyon ile
- [ ] NgRx: en az auth + cart store implement edilmiş
- [ ] Lazy loading tüm feature modüllerde aktif
- [ ] `any` tipi kullanımı minimize edilmiş
- [ ] `environment.ts` üzerinden base URL
- [ ] `README.md` → kurulum adımları yazılmış

---

## 🚀 Çalıştırma

```bash
cd frontend
npm install
ng serve --open
# → http://localhost:4200
```

**Backend çalışıyor olmalı:** `http://localhost:8080`

---

*CSE 214 — Advanced Application Development | Frontend Branch | 2026*
