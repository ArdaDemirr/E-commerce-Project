export interface CartItem {
  id: number;
  name: string;
  sku: string;
  unitPrice: number;
  qty: number;
  storeId: number;
  imageUrl?: string;
}
