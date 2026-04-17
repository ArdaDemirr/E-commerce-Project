export interface ShipmentResponseDTO {
  id: number;
  trackingId: string;
  mode: string;        // carrier: Flight, Ship, Road, etc.
  status: string;
  warehouse: string;
  productImportance: string;
  orderId: number;
}
