import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';

@Component({
  standalone: false,
  selector: 'app-cart-page',
  templateUrl: './cart-page.html',
  styleUrl: './cart-page.scss'
})
export class CartPageComponent implements OnInit {
  cartItems: any[] = [];
  paymentMethod = 'CREDIT_CARD';
  isProcessing = false;

  get subtotal(): number {
    return this.cartItems.reduce((sum, i) => sum + i.unitPrice * i.qty, 0);
  }

  constructor(private api: ApiService, private router: Router) { }

  ngOnInit(): void {
    // Load cart items — replace with NgRx store
    this.cartItems = [
      { id: 1, name: 'Laptop Pro X', unitPrice: 1299.99, qty: 1 },
      { id: 2, name: 'Wireless Mouse', unitPrice: 49.99, qty: 2 }
    ];
  }

  increaseQty(item: any): void { item.qty++; }

  decreaseQty(item: any): void {
    if (item.qty > 1) item.qty--;
    else this.removeItem(item);
  }

  removeItem(item: any): void {
    this.cartItems = this.cartItems.filter(c => c.id !== item.id);
  }

  checkout(): void {
    if (!this.cartItems.length) return;
    this.isProcessing = true;
    const payload = {
      items: this.cartItems.map(i => ({ productId: i.id, quantity: i.qty })),
      paymentMethod: this.paymentMethod
    };
    this.api.post('/orders', payload).subscribe({
      next: () => { this.cartItems = []; this.router.navigate(['/orders']); },
      error: () => { this.isProcessing = false; }
    });
  }
}
