import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError, tap } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { TokenService } from './token.service';
import { AuthResponse, LoginRequest, RegisterRequest, User, UserRole } from '../models/user.model';

const MOCK_USERS: Record<string, { password: string; user: User }> = {
    'admin@test.com': { password: '12345678', user: { id: 1, email: 'admin@test.com', firstName: 'Admin', lastName: 'User', role: 'ADMIN' as UserRole, isActive: true, createdAt: new Date().toISOString() } },
    'store@test.com': { password: '12345678', user: { id: 2, email: 'store@test.com', firstName: 'Corporate', lastName: 'User', role: 'CORPORATE' as UserRole, isActive: true, createdAt: new Date().toISOString() } },
    'user@test.com': { password: '12345678', user: { id: 3, email: 'user@test.com', firstName: 'Individual', lastName: 'User', role: 'INDIVIDUAL' as UserRole, isActive: true, createdAt: new Date().toISOString() } },
};

const USE_MOCK = true; // ← Backend hazır olunca false yap

@Injectable({ providedIn: 'root' })
export class AuthService {
    private apiUrl = environment.apiUrl;
    private currentUserSubject = new BehaviorSubject<User | null>(null);
    currentUser$ = this.currentUserSubject.asObservable();

    constructor(
        private http: HttpClient,
        private tokenService: TokenService,
        private router: Router
    ) {
        const user = this.tokenService.getUser();
        if (user) this.currentUserSubject.next(user);
    }

    login(req: LoginRequest): Observable<AuthResponse> {
        if (USE_MOCK) {
            const match = MOCK_USERS[req.email];
            if (match && match.password === req.password) {
                const mockRes: AuthResponse = {
                    accessToken: `mock-access-${match.user.role}`,
                    refreshToken: `mock-refresh-${match.user.role}`,
                    user: match.user
                };
                this.tokenService.setTokens(mockRes.accessToken, mockRes.refreshToken);
                this.tokenService.setUser(mockRes.user);
                this.currentUserSubject.next(mockRes.user);
                return of(mockRes);
            }
            return throwError(() => new Error('Geçersiz e-posta veya şifre'));
        }

        return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, req).pipe(
            tap((res: AuthResponse) => {
                this.tokenService.setTokens(res.accessToken, res.refreshToken);
                this.tokenService.setUser(res.user);
                this.currentUserSubject.next(res.user);
            })
        );
    }

    register(req: RegisterRequest): Observable<AuthResponse> {
        if (USE_MOCK) {
            const mockRes: AuthResponse = {
                accessToken: 'mock-access-INDIVIDUAL',
                refreshToken: 'mock-refresh-INDIVIDUAL',
                user: {
                    id: 99,
                    email: req.email,
                    firstName: req.firstName,
                    lastName: req.lastName,
                    role: req.role,
                    isActive: true,
                    createdAt: new Date().toISOString()
                }
            };
            this.tokenService.setTokens(mockRes.accessToken, mockRes.refreshToken);
            this.tokenService.setUser(mockRes.user);
            this.currentUserSubject.next(mockRes.user);
            return of(mockRes);
        }

        return this.http.post<AuthResponse>(`${this.apiUrl}/auth/register`, req).pipe(
            tap((res: AuthResponse) => {
                this.tokenService.setTokens(res.accessToken, res.refreshToken);
                this.tokenService.setUser(res.user);
                this.currentUserSubject.next(res.user);
            })
        );
    }

    logout(): void {
        if (!USE_MOCK) {
            this.http.post(`${this.apiUrl}/auth/logout`, {}).subscribe();
        }
        this.tokenService.clear();
        this.currentUserSubject.next(null);
        this.router.navigate(['/auth/login']);
    }

    refreshToken(): Observable<AuthResponse> {
        if (USE_MOCK) {
            const user = this.tokenService.getUser();
            const mockRes: AuthResponse = {
                accessToken: `mock-access-${user?.role || 'INDIVIDUAL'}`,
                refreshToken: `mock-refresh-${user?.role || 'INDIVIDUAL'}`,
                user
            };
            return of(mockRes);
        }

        return this.http.post<AuthResponse>(`${this.apiUrl}/auth/refresh`, {
            refreshToken: this.tokenService.getRefreshToken()
        }).pipe(
            tap((res: AuthResponse) => {
                this.tokenService.setTokens(res.accessToken, res.refreshToken);
            })
        );
    }

    get currentUser(): User | null { return this.currentUserSubject.value; }
    get isLoggedIn(): boolean { return this.tokenService.isLoggedIn(); }
    get userRole(): string { return this.currentUser?.role || ''; }
    hasRole(role: string): boolean { return this.userRole === role; }
}