import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';

export interface ChatMessage {
  role: 'user' | 'ai';
  content: string;
  chartType?: 'bar' | 'donut' | 'pie' | 'line' | null;
  chartSeries?: any[];
  chartLabels?: string[];
}

export interface ChatSession {
  id: string;
  title: string;
  messages: ChatMessage[];
}

export interface ChatRequest {
  message: string;
  history: ChatMessage[];
}

export interface ChatResponse {
  reply: string;
  blocked: boolean;
  hasChart?: boolean;
  chartData?: any;
}

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private apiUrl = `${environment.apiUrl}/chat`;

  private sessionsSubject = new BehaviorSubject<ChatSession[]>([]);
  public sessions$ = this.sessionsSubject.asObservable();

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  private getStorageKey(): string {
    const user = this.authService.currentUser;
    const userId = user ? user.id : 'guest';
    return `chatSessions_user_${userId}`;
  }

  ask(message: string, history: ChatMessage[] = []): Observable<ChatResponse> {
    return this.http.post<ChatResponse>(`${this.apiUrl}/ask`, {
      message,
      history,
    });
  }

  getSessions(): ChatSession[] {
    return this.sessionsSubject.value; // Returns latest stream snapshot synchronously when needed
  }

  loadInitialHistory(): void {
    const data = localStorage.getItem(this.getStorageKey());
    if (data) {
      this.sessionsSubject.next(JSON.parse(data));
    } else {
      this.sessionsSubject.next([]);
    }
  }

  saveSessions(sessions: ChatSession[]): void {
    this.sessionsSubject.next(sessions);
    localStorage.setItem(this.getStorageKey(), JSON.stringify(sessions));
  }

  deleteSession(sessionId: string): void {
    const currentSessions = this.sessionsSubject.value;
    const updatedSessions = currentSessions.filter((s) => s.id !== sessionId);
    this.saveSessions(updatedSessions);
  }
}
