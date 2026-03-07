export interface Category {
    id: number;
    name: string;
    parent?: Category;
}

export interface Product {
    id: number;
    sku: string;
    name: string;
    description?: string;
    unitPrice: number;
    stock: number;
    // Nested Spring Data REST response fields
    category?: Category;
    store?: { id: number; name: string };
    // Derived display fields
    categoryName?: string;
    storeName?: string;
    stockQty?: number; // alias for backward compat
    avgRating?: number;
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
