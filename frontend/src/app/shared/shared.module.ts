import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import {
    LucideAngularModule,
    Menu,
    Search,
    Bell,
    ChevronDown,
    User,
    LogOut,
    Moon,
    LayoutDashboard,
    Package,
    ShoppingCart,
    ShoppingBag,
    Truck,
    Star,
    PieChart,
    BarChart2,
    Settings,
    Users,
    Tags,
    Store,
    Zap,
    TrendingUp,
    TrendingDown,
    Download,
    Plus,
    Bot,
} from 'lucide-angular';

import { KpiCardComponent } from './components/kpi-card/kpi-card.component';

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        KpiCardComponent, // standalone component
        LucideAngularModule.pick({
            Menu,
            Search,
            Bell,
            ChevronDown,
            User,
            LogOut,
            Moon,
            LayoutDashboard,
            Package,
            ShoppingCart,
            ShoppingBag,
            Truck,
            Star,
            PieChart,
            BarChart2,
            Settings,
            Users,
            Tags,
            Store,
            Zap,
            TrendingUp,
            TrendingDown,
            Download,
            Plus,
            Bot,
        }),
    ],
    exports: [CommonModule, RouterModule, KpiCardComponent, LucideAngularModule],
})
export class SharedModule { }