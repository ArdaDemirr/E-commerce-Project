import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminUserService } from '../../../core/services/admin-user.service';
import { UserResponseDTO } from '../../../core/models/user.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin-stores',
  standalone: false,
  templateUrl: './admin-stores.html'
})
export class AdminStoresComponent implements OnInit {
  stores: UserResponseDTO[] = [];
  loading = true;

  constructor(
    private adminUserService: AdminUserService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadStores();
  }

  loadStores(): void {
    this.loading = true;
    this.adminUserService.getAllUsers().subscribe({
      next: (res) => {
        this.stores = res.filter(user => user.role === 'CORPORATE');
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.toastr.error('Mağazalar yüklenirken hata oluştu.');
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  toggleActive(store: UserResponseDTO): void {
    const newActiveState = !store.active;
    this.adminUserService.updateActive(store.id, newActiveState).subscribe({
      next: (updatedUser) => {
        store.active = updatedUser.active;
        this.toastr.success(`Mağaza durumu ${newActiveState ? 'Aktif' : 'Pasif'} olarak güncellendi.`);
        this.cdr.detectChanges();
      },
      error: () => this.toastr.error('Durum güncellenemedi.')
    });
  }
}
