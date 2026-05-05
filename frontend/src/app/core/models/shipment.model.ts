export interface ShipmentResponseDTO {
  id: number;
  trackingId: string;
  mode: string; // carrier: Flight, Ship, Road, etc.
  status: string;
  warehouse: string;
  productImportance: string;
  orderId: number;
  customer?: {
    id: number;
    name: string;
    surname: string;
    email: string;
  };
  productImageUrl?: string;
}
