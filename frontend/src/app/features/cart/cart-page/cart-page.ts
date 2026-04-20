import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import { CartService } from '../../../core/services/cart.service';
import { OrderService } from '../../../core/services/order.service';
import { CartItem } from '../../../core/models/cart.model';
import { ToastrService } from 'ngx-toastr';

import { environment } from '../../../../environments/environment';

declare var Stripe: any;

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

  stripe: any;
  cardElement: any;
  clientSecret: string = '';
  paymentStatus: string = '';

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
    if (typeof Stripe !== 'undefined') {
      this.stripe = Stripe(environment.stripePublishableKey);
      const elements = this.stripe.elements();
      this.cardElement = elements.create('card', {
        style: {
          base: {
            iconColor: '#94a3b8',
            color: '#f8fafc',
            fontWeight: '500',
            fontFamily: 'Inter, sans-serif',
            fontSize: '15px',
            fontSmoothing: 'antialiased',
            '::placeholder': { color: '#64748b' }
          },
          invalid: {
            iconColor: '#f87171',
            color: '#f87171'
          }
        }
      });
      setTimeout(() => {
        if (document.getElementById('card-element')) {
          this.cardElement.mount('#card-element');
        }
      }, 0);
    }

    this.cartService.items$
      .pipe(takeUntil(this.destroy$))
      .subscribe(items => {
        this.cartItems = items;
        this.cdr.detectChanges();
        this.updatePaymentIntent();
      });
  }

  private updatePaymentIntent() {
    // Toplam tutar kuruştan hesaplanıyor (1 TL = 100 kuruş)
    const amountInCents = Math.round(this.subtotal * 100);
    
    if (amountInCents > 0) {
      this.api.post<any>('/payment/create-intent', { amount: amountInCents })
        .subscribe({
          next: (res) => {
            this.clientSecret = res.clientSecret;
            this.cdr.detectChanges();
          },
          error: (err) => console.error('Intent oluşturulamadı:', err)
        });
    } else {
      this.clientSecret = '';
    }
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

  async checkout(): Promise<void> {
    if (!this.cartItems.length) return;
    this.isProcessing = true;
    this.paymentStatus = '';

    const storeId = this.cartItems[0]?.storeId;

    if (!storeId) {
      this.toastr.error('Bu ürün için mağaza bilgisi eksik. Lütfen sepeti temizleyip ürünü yeniden ekleyin.', 'Sipariş Hatası', { timeOut: 3000 });
      this.isProcessing = false;
      return;
    }

    if (this.paymentMethod === 'CREDIT_CARD') {
      const result = await this.stripe.confirmCardPayment(this.clientSecret, {
        payment_method: {
          card: this.cardElement
        }
      });

      if (result.error) {
        this.paymentStatus = 'Hata: ' + result.error.message;
        this.isProcessing = false;
        this.cdr.detectChanges();
        return; // Ödeme hatası, işlemi durdur
      } else if (result.paymentIntent && result.paymentIntent.status === 'succeeded') {
        this.paymentStatus = '✅ Ödeme başarılı! Sipariş oluşturuluyor...';
      }
    }

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
