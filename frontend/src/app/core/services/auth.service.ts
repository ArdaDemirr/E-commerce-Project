import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { TokenService } from './token.service';
import { AuthResponse, LoginRequest, RegisterRequest, User } from '../models/user.model';

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
        return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, req).pipe(
            tap((res: AuthResponse) => {
                this.tokenService.setTokens(res.accessToken, res.refreshToken);
                this.tokenService.setUser(res.user);
                this.currentUserSubject.next(res.user);
            })
        );
    }

    register(req: RegisterRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/auth/register`, req).pipe(
            tap((res: AuthResponse) => {
                this.tokenService.setTokens(res.accessToken, res.refreshToken);
                this.tokenService.setUser(res.user);
                this.currentUserSubject.next(res.user);
            })
        );
    }

    logout(): void {
        this.http.post(`${this.apiUrl}/auth/logout`, {}).subscribe();
        this.tokenService.clear();
        this.currentUserSubject.next(null);
        this.router.navigate(['/auth/login']);
    }

    refreshToken(): Observable<AuthResponse> {
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
