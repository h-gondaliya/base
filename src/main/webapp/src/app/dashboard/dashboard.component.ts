import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

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

  constructor(private router: Router) {}

  ngOnInit() {
    this.token = localStorage.getItem('authToken') || '';

    // If no token, redirect to login
    if (!this.token) {
      this.router.navigate(['/login']);
      return;
    }

    // Decode username from token (simple implementation for demo)
    try {
      const payload = JSON.parse(atob(this.token.split('.')[1]));
      this.username = payload.sub || 'User';
    } catch (error) {
      this.username = 'User';
    }
  }

  logout() {
    localStorage.removeItem('authToken');
    this.router.navigate(['/login']);
  }
}
