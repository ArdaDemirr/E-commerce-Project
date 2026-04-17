import { Component, OnInit } from '@angular/core';
import { ShipmentService } from '../../../core/services/shipment.service';
import { ShipmentResponseDTO } from '../../../core/models/shipment.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-store-shipments',
  standalone: false,
  templateUrl: './store-shipments.html',
  styleUrl: './store-shipments.scss'
})
export class StoreShipmentsComponent implements OnInit {
  shipments: ShipmentResponseDTO[] = [];
  loading = true;

  constructor(
    private shipmentService: ShipmentService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadShipments();
  }

  loadShipments(): void {
    this.loading = true;
    this.shipmentService.getMyStoreShipments().subscribe({
      next: (data) => {
        this.shipments = data;
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Gönderiler yüklenirken hata oluştu.');
        this.loading = false;
      }
    });
  }

  updateStatus(id: number, status: string): void {
    if (!status) return;

    this.shipmentService.updateShipmentStatus(id, status).subscribe({
      next: () => {
        this.toastr.success('Gönderi durumu güncellendi.');
        this.loadShipments();
      },
      error: () => {
        this.toastr.error('Gönderi durumu güncellenemedi.');
      }
    });
  }
}
