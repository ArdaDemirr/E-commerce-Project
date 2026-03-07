import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, filter, switchMap, take } from 'rxjs/operators';
import { TokenService } from '../services/token.service';
import { AuthService } from '../services/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    private isRefreshing = false;
    private refreshTokenSubject = new BehaviorSubject<string | null>(null);

    constructor(private tokenService: TokenService, private authService: AuthService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = this.tokenService.getAccessToken();
        const authReq = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

        return next.handle(authReq).pipe(
            catchError((err: HttpErrorResponse) => {
                if (err.status === 401 && !req.url.includes('/auth/')) {
                    return this.handle401(authReq, next);
                }
                return throwError(() => err);
            })
        );
    }

    private handle401(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!this.isRefreshing) {
            this.isRefreshing = true;
            this.refreshTokenSubject.next(null);
            return this.authService.refreshToken().pipe(
                switchMap(res => {
                    this.isRefreshing = false;
                    this.refreshTokenSubject.next(res.token);
                    return next.handle(req.clone({ setHeaders: { Authorization: `Bearer ${res.token}` } }));
                }),
                catchError(err => {
                    this.isRefreshing = false;
                    this.authService.logout();
                    return throwError(() => err);
                })
            );
        }
        return this.refreshTokenSubject.pipe(
            filter(t => t !== null), take(1),
            switchMap(t => next.handle(req.clone({ setHeaders: { Authorization: `Bearer ${t}` } })))
        );
    }
}
