import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Get the auth token from the service
    const authToken = this.authService.getToken();

    // Clone the request and add the authorization header if token exists
    const authReq = authToken
      ? req.clone({
          setHeaders: {
            Authorization: `Bearer ${authToken}`,
          },
        })
      : req;

    // Pass the cloned request and handle authentication failures
    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        // Handle authentication failures (401 Unauthorized, 403 Forbidden)
        if (error.status === 401 || error.status === 403) {
          // Clear the invalid token
          this.authService.removeToken();
          // Redirect to login page
          this.router.navigate(['/login']);
        }
        // Re-throw the error so components can still handle it if needed
        return throwError(() => error);
      })
    );
  }
}
