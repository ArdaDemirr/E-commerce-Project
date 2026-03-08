import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, catchError, of } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Product, Category } from '../models/product.model';

// ─── MOCK DATA (mirrors actual DB) ────────────────────────────────────────────
// When the backend exposes /api/products and /api/categories, set USE_MOCK=false
const USE_MOCK = false;

const MOCK_CATEGORIES: Category[] = [
    { id: 1, name: 'Beauty & Grooming' },
    { id: 2, name: 'Mobiles & Tablets' },
    { id: 3, name: 'Home & Living' },
    { id: 4, name: 'Appliances' },
    { id: 5, name: "Men's Fashion" },
    { id: 6, name: 'Kids & Baby' },
    { id: 7, name: 'Soghaat' },
    { id: 8, name: 'Others' },
];

const MOCK_PRODUCTS: Product[] = [
    { id: 4, sku: 'kcc_krone_deal', name: 'kcc_krone deal', unitPrice: 149.99, stock: 30, description: 'Beauty & Grooming ürünü', category: MOCK_CATEGORIES[0], categoryName: 'Beauty & Grooming', stockQty: 30 },
    { id: 5, sku: 'BK7010400AG', name: 'BK7010400AG', unitPrice: 299.00, stock: 15, description: 'Soghaat koleksiyonu', category: MOCK_CATEGORIES[6], categoryName: 'Soghaat', stockQty: 15 },
    { id: 6, sku: 'UK_Namkino_200G', name: 'UK_Namkino All In One 200 Gms', unitPrice: 89.50, stock: 50, description: 'Doğal ürün', category: MOCK_CATEGORIES[6], categoryName: 'Soghaat', stockQty: 50 },
    { id: 7, sku: 'UK_Namkino_400G', name: 'UK_Namkino Mix Nimco 400 Gms', unitPrice: 120.00, stock: 40, description: 'Karışık paket', category: MOCK_CATEGORIES[6], categoryName: 'Soghaat', stockQty: 40 },
    { id: 8, sku: 'Apple_iPhone_6S_64GB', name: 'Apple iPhone 6S 64GB', unitPrice: 1899.99, stock: 5, description: 'Akıllı telefon', category: MOCK_CATEGORIES[1], categoryName: 'Mobiles & Tablets', stockQty: 5 },
    { id: 9, sku: 'GFC_Myga_Cross', name: 'GFC Pedestal Myga Cross Base', unitPrice: 450.00, stock: 12, description: 'Ev aletleri', category: MOCK_CATEGORIES[3], categoryName: 'Appliances', stockQty: 12 },
    { id: 10, sku: 'BK1070200PL', name: 'BK1070200PL', unitPrice: 199.00, stock: 25, description: 'Soghaat ürünü', category: MOCK_CATEGORIES[6], categoryName: 'Soghaat', stockQty: 25 },
    { id: 12, sku: 'kcc_Sultanat', name: 'kcc_Sultanat', unitPrice: 175.00, stock: 20, description: 'Ev & Yaşam ürünü', category: MOCK_CATEGORIES[2], categoryName: 'Home & Living', stockQty: 20 },
    { id: 13, sku: 'kcc_glamour_deal', name: 'kcc_glamour deal', unitPrice: 159.00, stock: 35, description: 'Güzellik ürünü', category: MOCK_CATEGORIES[0], categoryName: 'Beauty & Grooming', stockQty: 35 },
    { id: 14, sku: 'Assetmen_MD346M', name: 'Assetmen MD-346-M', unitPrice: 340.00, stock: 8, description: 'Erkek modası', category: MOCK_CATEGORIES[4], categoryName: "Men's Fashion", stockQty: 8 },
    { id: 15, sku: 'cr_DATES_CASHEW', name: 'cr-DATES WITH CASHEW-400 GM', unitPrice: 95.00, stock: 60, description: 'Özel tarihi ürün', category: MOCK_CATEGORIES[6], categoryName: 'Soghaat', stockQty: 60 },
    { id: 16, sku: 'UK_Gift_Box_Dry', name: 'UK_Gift Box Mix Dry Fruit Sweets 500 Gms', unitPrice: 210.00, stock: 22, description: 'Hediye kutusu', category: MOCK_CATEGORIES[6], categoryName: 'Soghaat', stockQty: 22 },
    { id: 17, sku: 'itter_AB_1199', name: 'itter_AB 1199', unitPrice: 329.00, stock: 15, description: 'Güzellik seti', category: MOCK_CATEGORIES[0], categoryName: 'Beauty & Grooming', stockQty: 15 },
    { id: 18, sku: 'RL_B005', name: 'RL_B005', unitPrice: 180.00, stock: 30, description: 'Ev ürünü', category: MOCK_CATEGORIES[2], categoryName: 'Home & Living', stockQty: 30 },
    { id: 19, sku: 'bed_rest_S7', name: 'bed&rest_S7', unitPrice: 220.00, stock: 18, description: 'Ev konforu', category: MOCK_CATEGORIES[2], categoryName: 'Home & Living', stockQty: 18 },
    { id: 22, sku: 'JJ_JR_20', name: 'J&J_JJR-20', unitPrice: 75.00, stock: 45, description: 'Çocuk ürünü', category: MOCK_CATEGORIES[5], categoryName: 'Kids & Baby', stockQty: 45 },
    { id: 23, sku: 'D_Lend_Helping_Hand', name: 'D Lend a Helping Hand', unitPrice: 115.00, stock: 3, description: 'Diğer ürünler', category: MOCK_CATEGORIES[7], categoryName: 'Others', stockQty: 3 },
    { id: 24, sku: 'Mochika_M0001112_12', name: 'Mochika_M0001112-12', unitPrice: 260.00, stock: 10, description: 'Erkek modası', category: MOCK_CATEGORIES[4], categoryName: "Men's Fashion", stockQty: 10 },
];
// ──────────────────────────────────────────────────────────────────────────────

@Injectable({ providedIn: 'root' })
export class ProductService {
    private base = environment.apiUrl;

    constructor(private http: HttpClient) { }

    getProducts(): Observable<Product[]> {
        if (USE_MOCK) return of(MOCK_PRODUCTS);

        return this.http.get<any>(`${this.base}/products`).pipe(
            map(res => {
                if (Array.isArray(res)) return this.mapProducts(res);
                if (res?._embedded?.products) return this.mapProducts(res._embedded.products);
                if (res?.content) return this.mapProducts(res.content);
                return [];
            }),
            catchError((err) => {
                console.error('Failed to fetch products from DB:', err);
                return of(MOCK_PRODUCTS);
            })
        );
    }

    getCategories(): Observable<Category[]> {
        if (USE_MOCK) return of(MOCK_CATEGORIES);

        return this.http.get<any>(`${this.base}/categories`).pipe(
            map(res => {
                if (Array.isArray(res)) return res as Category[];
                if (res?._embedded?.categories) return res._embedded.categories as Category[];
                return [];
            }),
            catchError((err) => {
                console.error('Failed to fetch categories:', err);
                return of(MOCK_CATEGORIES);
            })
        );
    }

    getProduct(id: number): Observable<Product | null> {
        if (USE_MOCK) return of(MOCK_PRODUCTS.find(p => p.id === id) ?? null);

        return this.http.get<Product>(`${this.base}/products/${id}`).pipe(
            map(p => this.mapProduct(p)),
            catchError((err) => {
                console.error('Failed to fetch product:', err);
                return of(null);
            })
        );
    }

    searchProducts(query: string): Observable<Product[]> {
        if (USE_MOCK) {
            const q = query.toLowerCase();
            return of(MOCK_PRODUCTS.filter(p =>
                p.name.toLowerCase().includes(q) || p.sku.toLowerCase().includes(q)
            ));
        }

        return this.http.get<any>(`${this.base}/products/search?name=${encodeURIComponent(query)}`).pipe(
            map(res => Array.isArray(res) ? this.mapProducts(res) : []),
            catchError((err) => {
                console.error('Failed to search products:', err);
                return of([]);
            })
        );
    }

    private mapProducts(raw: any[]): Product[] {
        return raw.map(p => this.mapProduct(p));
    }

    private mapProduct(p: any): Product {
        return {
            ...p,
            stock: p.stock ?? p.stockQty ?? 0,
            stockQty: p.stock ?? p.stockQty ?? 0,
            categoryName: p.category?.name ?? p.categoryName ?? 'Diğer',
            storeName: p.store?.name ?? p.storeName ?? '',
        };
    }
}
