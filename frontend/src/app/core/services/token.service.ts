import { Injectable } from '@angular/core';

const ACCESS_KEY = 'access_token';
const REFRESH_KEY = 'refresh_token';
const USER_KEY = 'current_user';

@Injectable({ providedIn: 'root' })
export class TokenService {
    setTokens(access: string, refresh: string): void {
        localStorage.setItem(ACCESS_KEY, access);
        localStorage.setItem(REFRESH_KEY, refresh);
    }
    getAccessToken(): string | null { return localStorage.getItem(ACCESS_KEY); }
    getRefreshToken(): string | null { return localStorage.getItem(REFRESH_KEY); }
    setUser(user: any): void { localStorage.setItem(USER_KEY, JSON.stringify(user)); }
    getUser(): any {
        const u = localStorage.getItem(USER_KEY);
        return u ? JSON.parse(u) : null;
    }
    clear(): void {
        localStorage.removeItem(ACCESS_KEY);
        localStorage.removeItem(REFRESH_KEY);
        localStorage.removeItem(USER_KEY);
    }
    isLoggedIn(): boolean { return !!this.getAccessToken(); }
}
