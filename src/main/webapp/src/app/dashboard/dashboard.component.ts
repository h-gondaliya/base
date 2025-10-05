import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  username: string = '';
  token: string = '';

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    this.token = this.authService.getToken() || '';

    // If no token, redirect to login
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    // Get username from AuthService
    this.username = this.authService.getUsername();
  }

  logout() {
    this.authService.removeToken();
    this.router.navigate(['/login']);
  }
}
