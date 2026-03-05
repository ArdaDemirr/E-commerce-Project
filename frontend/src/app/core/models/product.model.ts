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
