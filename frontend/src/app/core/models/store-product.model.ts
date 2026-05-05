export interface StoreProductsRequestDTO {
  name: string;
  sku: string;
  unitPrice: number;
  stock: number;
  description: string;
  categoryId: number;
  imageUrl?: string;
}

export interface StoreProductsResponseDTO {
  id: number;
  name: string;
  sku: string;
  unitPrice: number;
  stock: number;
  categoryName: string;
  isLowStock: boolean;
  description: string;
  categoryId: number;
  imageUrl?: string;
}
