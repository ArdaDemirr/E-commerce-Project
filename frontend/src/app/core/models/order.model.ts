export type OrderStatus = 'PENDING' | 'SHIPPED' | 'COMPLETED' | 'CANCELLED';

export interface Order {
  id: number;
  userId: number;
  storeId: number;
  status: OrderStatus;
  grandTotal: number;
  paymentMethod: string;
  createdAt: string;
  items: OrderItem[];
}

export interface OrderItem {
  id: number;
  productId: number;
  productName: string;
  quantity: number;
  price: number;
  productImageUrl?: string;
}

export interface CartItem {
  product: import('./product.model').Product;
  quantity: number;
}
