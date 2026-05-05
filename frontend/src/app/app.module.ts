import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import {
  LucideAngularModule,
  Zap,
  LayoutDashboard,
  Package,
  Bot,
  ShoppingCart,
  ShoppingBag,
  Truck,
  Star,
  BarChart2,
  Settings,
  Users,
  Tags,
  Store,
  Moon,
  Activity,
} from 'lucide-angular';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SharedModule } from './shared/shared.module';
import { NgApexchartsModule } from 'ng-apexcharts';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { SidebarComponent } from './shared/components/sidebar/sidebar.component';
import { JwtInterceptor } from './core/interceptors/jwt.interceptor';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    MainLayoutComponent,
    AuthLayoutComponent,
    NavbarComponent,
    SidebarComponent,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
    }),
    RouterModule,
    SharedModule,
    NgApexchartsModule,
    LucideAngularModule.pick({
      Zap,
      LayoutDashboard,
      Package,
      Bot,
      ShoppingCart,
      ShoppingBag,
      Truck,
      Star,
      BarChart2,
      Settings,
      Users,
      Tags,
      Store,
      Moon,
      Activity,
    }),
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
