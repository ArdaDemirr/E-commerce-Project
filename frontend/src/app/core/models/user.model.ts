export type UserRole = 'ADMIN' | 'CORPORATE' | 'INDIVIDUAL';

export interface User {
    id: number;
    email: string;
    name: string;
    surname: string;
    role: UserRole;
    active: boolean;
}

export interface UserResponseDTO {
    id: number;
    email: string;
    role: string;
    name: string;
    surname: string;
    gender: string;
    active: boolean;
}

export interface AuthResponse {
    // token is NO LONGER here — backend sets it as an HttpOnly cookie
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
