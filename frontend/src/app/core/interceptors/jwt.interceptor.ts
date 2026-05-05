import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError, BehaviorSubject, switchMap, filter, take, catchError } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  // This flag prevents multiple simultaneous refresh requests
  private isRefreshing = false;
  private refreshDone$ = new BehaviorSubject<boolean>(false);

  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Add withCredentials: true so the browser automatically sends HttpOnly cookies
    // No need to manually add Authorization header — the cookie is sent automatically
    const authReq = req.clone({ withCredentials: true });

    return next.handle(authReq).pipe(
      catchError((err: HttpErrorResponse) => {
        // If we get a 401 and this isn't a refresh/login request itself, try to refresh
        if (err.status === 401 && !req.url.includes('/auth/')) {
          return this.handle401(authReq, next);
        }
        return throwError(() => err);
      })
    );
  }

  private handle401(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isRefreshing) {
      // Start refreshing — prevent other requests from also trying to refresh
      this.isRefreshing = true;
      this.refreshDone$.next(false);

      return this.authService.refreshToken().pipe(
        switchMap(() => {
          // Refresh succeeded — notify waiting requests and retry the original
          this.isRefreshing = false;
          this.refreshDone$.next(true);
          return next.handle(req);
        }),
        catchError((refreshErr) => {
          // Refresh failed — log the user out
          this.isRefreshing = false;
          this.authService.logout();
          return throwError(() => refreshErr);
        })
      );
    } else {
      // Another request is already refreshing — wait for it to finish, then retry
      return this.refreshDone$.pipe(
        filter(done => done === true),
        take(1),
        switchMap(() => next.handle(req))
      );
    }
  }
}

