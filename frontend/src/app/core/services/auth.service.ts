import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError, tap } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { TokenService } from './token.service';
import {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
  User,
  UserRole,
} from '../models/user.model';

// ── Mock users for offline development ──────────────────────────────────────────
const MOCK_USERS: Record<string, { password: string; user: User }> = {
  'admin@test.com': {
    password: '12345678',
    user: { id: 1, email: 'admin@test.com', name: 'Admin', surname: 'User', role: 'ADMIN' as UserRole, active: true },
  },
  'store@test.com': {
    password: '12345678',
    user: { id: 2, email: 'store@test.com', name: 'Corporate', surname: 'User', role: 'CORPORATE' as UserRole, active: true },
  },
  'user@test.com': {
    password: '12345678',
    user: { id: 3, email: 'user@test.com', name: 'Individual', surname: 'User', role: 'INDIVIDUAL' as UserRole, active: true },
  },
};

const USE_MOCK = false;

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = environment.apiUrl;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    private router: Router,
  ) {
    // Restore user from sessionStorage on app start
    const user = this.tokenService.getUser();
    if (user) this.currentUserSubject.next(user);
  }

  // ── Login ─────────────────────────────────────────────────────────────────────
  login(req: LoginRequest): Observable<AuthResponse> {
    if (USE_MOCK) {
      const match = MOCK_USERS[req.email];
      if (match && match.password === req.password) {
        const mockRes: AuthResponse = {
          role: match.user.role,
          name: match.user.name,
          surname: match.user.surname,
          userId: match.user.id,
        };
        this.tokenService.setUser(match.user);
        this.currentUserSubject.next(match.user);
        return of(mockRes);
      }
      return throwError(() => new Error('Geçersiz e-posta veya şifre'));
    }

    // withCredentials: true → browser will save the HttpOnly cookies from Set-Cookie response header
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, req, { withCredentials: true }).pipe(
      tap((res: AuthResponse) => {
        const user: User = {
          id: res.userId,
          email: req.email,
          name: res.name,
          surname: res.surname,
          role: res.role as UserRole,
          active: true,
        };
        // Only store metadata — token is safely in an HttpOnly cookie
        this.tokenService.setUser(user);
        this.currentUserSubject.next(user);
      }),
    );
  }

  // ── Register ──────────────────────────────────────────────────────────────────
  register(req: RegisterRequest): Observable<any> {
    if (USE_MOCK) {
      const mockRes: AuthResponse = {
        role: req.role,
        name: req.name,
        surname: req.surname,
        userId: 99,
      };
      const mockUser: User = { id: 99, email: req.email, name: req.name, surname: req.surname, role: req.role, active: true };
      this.tokenService.setUser(mockUser);
      this.currentUserSubject.next(mockUser);
      return of(mockRes);
    }

    return this.http
      .post(`${this.apiUrl}/auth/register`, req, { responseType: 'text' })
      .pipe(
        tap(() => {
          this.router.navigate(['/auth/login']);
        }),
      );
  }

  // ── Logout ────────────────────────────────────────────────────────────────────
  logout(): void {
    // Tell the backend to clear the HttpOnly cookies
    this.http.post(`${this.apiUrl}/auth/logout`, {}, { withCredentials: true }).subscribe({
      complete: () => {
        this.tokenService.clear();
        this.currentUserSubject.next(null);
        this.router.navigate(['/auth/login']);
      },
      error: () => {
        // Even if the request fails, clear local state and redirect
        this.tokenService.clear();
        this.currentUserSubject.next(null);
        this.router.navigate(['/auth/login']);
      }
    });
  }

  // ── Refresh Token ─────────────────────────────────────────────────────────────
  // Calls /api/auth/refresh — browser automatically sends the refresh_token cookie.
  // Backend validates it and sets a new access_token cookie.
  refreshToken(): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/refresh`, {}, { withCredentials: true });
  }

  // ── Getters ───────────────────────────────────────────────────────────────────
  get currentUser(): User | null {
    return this.currentUserSubject.value;
  }
  get isLoggedIn(): boolean {
    return this.tokenService.isLoggedIn();
  }
  get userRole(): string {
    return this.currentUser?.role || '';
  }
  hasRole(role: string): boolean {
    return this.userRole === role;
  }
}

