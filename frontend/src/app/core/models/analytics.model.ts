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
