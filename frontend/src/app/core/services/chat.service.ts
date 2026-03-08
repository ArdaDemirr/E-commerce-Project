import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { TokenService } from './token.service';

export interface ChatRequest {
    message: string;
}

export interface ChatResponse {
    reply: string;
    blocked: boolean;
}

@Injectable({
    providedIn: 'root',
})
export class ChatService {
    private apiUrl = `${environment.apiUrl}/chat`;

    constructor(
        private http: HttpClient,
        private tokenService: TokenService,
    ) { }

    ask(message: string): Observable<ChatResponse> {
        const token = this.tokenService.getAccessToken();
        const headers = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : null;
        const options = headers ? { headers } : {};
        return this.http.post<ChatResponse>(`${this.apiUrl}/ask`, { message }, options);
    }
}
