import { Injectable } from '@angular/core';

// Tokens (access + refresh) are now HttpOnly cookies — the browser manages them automatically.
// This service only stores non-sensitive user METADATA (name, role, userId) in sessionStorage.
// sessionStorage is cleared when the browser tab/window is closed (more secure than localStorage).

const USER_KEY = 'current_user';

@Injectable({ providedIn: 'root' })
export class TokenService {

    // ── User Metadata ─────────────────────────────────────────────────────────────
    setUser(user: any): void {
        sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    }

    getUser(): any {
        const u = sessionStorage.getItem(USER_KEY);
        return u ? JSON.parse(u) : null;
    }

    clear(): void {
        sessionStorage.removeItem(USER_KEY);
    }

    // A user is considered "logged in" if their metadata exists in sessionStorage.
    // The actual auth proof is the HttpOnly access_token cookie (invisible to JS).
    isLoggedIn(): boolean {
        return !!this.getUser();
    }
}
