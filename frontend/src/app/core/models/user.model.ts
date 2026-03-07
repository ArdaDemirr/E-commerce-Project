export type UserRole = 'ADMIN' | 'CORPORATE' | 'INDIVIDUAL';

export interface User {
    id: number;
    email: string;
    name: string;
    surname: string;
    role: UserRole;
    active: boolean;
}

export interface AuthResponse {
    token: string;
    role: string;
    name: string;
    surname: string;
    userId: number;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    email: string;
    password: string;
    name: string;
    surname: string;
    role: UserRole;
    gender?: string;
}
