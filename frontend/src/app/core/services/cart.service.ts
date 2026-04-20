import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CartItem } from '../models/cart.model';
import { Product } from '../models/product.model';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class CartService {
  private currentCartKey = 'dp_cart_guest';
  private _items$ = new BehaviorSubject<CartItem[]>([]);

  constructor(private authService: AuthService) {
    this.authService.currentUser$.subscribe(user => {
      this.currentCartKey = user && user.id ? `dp_cart_${user.id}` : 'dp_cart_guest';
      this._items$.next(this.loadFromStorage());
    });
  }

  /** Observable list of cart items */
  items$: Observable<CartItem[]> = this._items$.asObservable();

  /** Observable item count for badge */
  count$: Observable<number> = this._items$.pipe(
    map((items) => items.reduce((s, i) => s + i.qty, 0)),
  );

  get items(): CartItem[] {
    return this._items$.getValue();
  }

  /** Add a product to cart (or increment qty if already there) */
  addItem(product: Product): void {
    const current = this.items;
    const existing = current.find((i) => i.id === product.id);
    let updated: CartItem[];
    if (existing) {
      updated = current.map((i) =>
        i.id === product.id ? { ...i, qty: i.qty + 1 } : i,
      );
    } else {
      const newItem: CartItem = {
        id: product.id,
        name: product.name,
        sku: product.sku || '',
        unitPrice: product.unitPrice,
        qty: 1,
        storeId: product.store?.id || 0,
        imageUrl: product.imageUrl,
      };
      updated = [...current, newItem];
    }
    this.update(updated);
  }

  increaseQty(id: number): void {
    this.update(
      this.items.map((i) => (i.id === id ? { ...i, qty: i.qty + 1 } : i)),
    );
  }

  decreaseQty(id: number): void {
    const updated = this.items
      .map((i) => (i.id === id ? { ...i, qty: i.qty - 1 } : i))
      .filter((i) => i.qty > 0);
    this.update(updated);
  }

  removeItem(id: number): void {
    this.update(this.items.filter((i) => i.id !== id));
  }

  clearCart(): void {
    this.update([]);
  }

  get subtotal(): number {
    return this.items.reduce((s, i) => s + i.unitPrice * i.qty, 0);
  }

  private update(items: CartItem[]): void {
    this._items$.next(items);
    localStorage.setItem(this.currentCartKey, JSON.stringify(items));
  }

  private loadFromStorage(): CartItem[] {
    try {
      const raw = localStorage.getItem(this.currentCartKey);
      return raw ? JSON.parse(raw) : [];
    } catch {
      return [];
    }
  }
}
