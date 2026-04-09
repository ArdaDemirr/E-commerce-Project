import { Component } from '@angular/core';

@Component({
  standalone: false,
  selector: 'app-user-management',
  template: `
    <div class="space-y-6">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-2xl font-bold text-white">Kullanıcı Yönetimi</h1>
          <p class="text-slate-400 text-sm mt-1">Bu sayfa yakında eklenecektir.</p>
        </div>
      </div>
      <div class="glass-card p-12 text-center">
        <lucide-icon name="users" [size]="48" class="text-slate-600 mx-auto mb-4"></lucide-icon>
        <p class="text-slate-400 font-medium">Kullanıcı yönetimi modülü yapım aşamasındadır.</p>
      </div>
    </div>
  `
})
export class UserManagementComponent {}
