import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
    constructor(private auth: AuthService, private router: Router) { }
    canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
        const requiredRoles: string[] = route.data['roles'] || [];
        if (requiredRoles.includes(this.auth.userRole)) return true;
        return this.router.createUrlTree(['/unauthorized']);
    }
}
