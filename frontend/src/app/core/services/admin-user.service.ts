import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { UserResponseDTO } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class AdminUserService {
  constructor(private api: ApiService) {}

  getAllUsers(): Observable<UserResponseDTO[]> {
    return this.api.get<UserResponseDTO[]>('/admin/users');
  }

  updateRole(id: number, role: string): Observable<UserResponseDTO> {
    return this.api.put<UserResponseDTO>(`/admin/users/${id}/role`, { role });
  }

  updateActive(id: number, active: boolean): Observable<UserResponseDTO> {
    return this.api.put<UserResponseDTO>(`/admin/users/${id}/active`, { active });
  }

  deleteUser(id: number): Observable<void> {
    return this.api.delete<void>(`/admin/users/${id}`);
  }
}
