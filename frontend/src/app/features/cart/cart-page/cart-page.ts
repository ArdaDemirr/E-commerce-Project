import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import { CartService } from '../../../core/services/cart.service';
import { OrderService } from '../../../core/services/order.service';
import { CartItem } from '../../../core/models/cart.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  standalone: false,
  selector: 'app-cart-page',
  templateUrl: './cart-page.html',
  styleUrl: './cart-page.scss'
})
export class CartPageComponent implements OnInit, OnDestroy {
  cartItems: CartItem[] = [];
  paymentMethod = 'CREDIT_CARD';
  isProcessing = false;

  private destroy$ = new Subject<void>();

  get subtotal(): number {
    return this.cartItems.reduce((sum, i) => sum + i.unitPrice * i.qty, 0);
  }

  constructor(
    private api: ApiService,
    private router: Router,
    private cartService: CartService,
    private orderService: OrderService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.cartService.items$
      .pipe(takeUntil(this.destroy$))
      .subscribe(items => {
        this.cartItems = items;
        this.cdr.detectChanges();
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  increaseQty(item: CartItem): void {
    this.cartService.increaseQty(item.id);
  }

  decreaseQty(item: CartItem): void {
    this.cartService.decreaseQty(item.id);
  }

  removeItem(item: CartItem): void {
    this.cartService.removeItem(item.id);
    this.toastr.info(`"${item.name}" sepetten çıkarıldı.`, '', { timeOut: 1800 });
  }

  checkout(): void {
    if (!this.cartItems.length) return;
    this.isProcessing = true;

    const storeId: number = this.cartItems[0]?.storeId ?? 1;
    const payload = {
      storeId,
      paymentMethod: this.paymentMethod,
      items: this.cartItems.map(i => ({ productId: i.id, quantity: i.qty })),
    };

    this.orderService.placeOrder(payload).subscribe({
      next: () => {
        this.cartService.clearCart();
        this.isProcessing = false;
        this.toastr.success('Siparişiniz başarıyla alındı! 🎉', 'Sipariş Verildi', {
          timeOut: 3000,
          progressBar: true,
        });
        this.router.navigate(['/orders']);
      },
      error: (err) => {
        this.isProcessing = false;
        const msg = err?.error?.message || 'Sipariş alınamadı. Lütfen tekrar deneyin.';
        this.toastr.error(msg, 'Hata');
      }
    });
  }
}
