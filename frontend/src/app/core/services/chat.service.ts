import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

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

    constructor(private http: HttpClient) { }

    ask(message: string): Observable<ChatResponse> {
        // withCredentials: true is handled globally by JwtInterceptor
        // The access_token HttpOnly cookie is sent automatically by the browser
        return this.http.post<ChatResponse>(`${this.apiUrl}/ask`, { message });
    }
}
