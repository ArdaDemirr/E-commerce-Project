import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminUserService } from '../../../core/services/admin-user.service';
import { UserResponseDTO } from '../../../core/models/user.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-management',
  standalone: false,
  templateUrl: './user-management.html'
})
export class UserManagementComponent implements OnInit {
  users: UserResponseDTO[] = [];
  loading = true;

  constructor(
    private adminUserService: AdminUserService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.adminUserService.getAllUsers().subscribe({
      next: (res) => {
        this.users = res;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.toastr.error('Kullanıcılar yüklenirken bir hata oluştu');
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  toggleActive(user: UserResponseDTO): void {
    const newActiveState = !user.active;
    this.adminUserService.updateActive(user.id, newActiveState).subscribe({
      next: (updatedUser) => {
        user.active = updatedUser.active;
        this.toastr.success(`Kullanıcı durumu ${newActiveState ? 'Aktif' : 'Pasif'} olarak güncellendi.`);
        this.cdr.detectChanges();
      },
      error: () => this.toastr.error('Durum güncellenemedi.')
    });
  }

  changeRole(user: UserResponseDTO, newRole: string): void {
    if (user.role === newRole) return;
    this.adminUserService.updateRole(user.id, newRole).subscribe({
      next: (updatedUser) => {
        user.role = updatedUser.role;
        this.toastr.success('Kullanıcı rolü güncellendi.');
        this.cdr.detectChanges();
      },
      error: () => this.toastr.error('Rol güncellenemedi.')
    });
  }

  deleteUser(id: number): void {
    if (!confirm('Bu kullanıcıyı tamamen silmek istediğinize emin misiniz?')) return;

    this.adminUserService.deleteUser(id).subscribe({
      next: () => {
        this.users = this.users.filter(u => u.id !== id);
        this.toastr.success('Kullanıcı başarıyla silindi.');
        this.cdr.detectChanges();
      },
      error: () => this.toastr.error('Kullanıcı silinemedi.')
    });
  }
}
