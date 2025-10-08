import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { ApiService, ClientApprovalStats } from '../services/api.service';

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
  availableProductsCount: number = 0;
  approvalStatsByClient: ClientApprovalStats[] = [];
  isLoadingStats: boolean = false;

  constructor(private router: Router, private authService: AuthService, private productApiService: ApiService) {}

  ngOnInit() {
    this.token = this.authService.getToken() || '';

    // If no token, redirect to login
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    // Get username from AuthService
    this.username = this.authService.getUsername();

    // Load dashboard data
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.isLoadingStats = true;
    this.loadAvailableProductsCount();
    this.loadApprovalStatsByClient();
  }

  loadAvailableProductsCount() {
    this.productApiService.getAvailableProductsCount()
      .subscribe({
        next: (count) => {
          this.availableProductsCount = count;
        },
        error: (error) => {
          console.error('Error loading available products count:', error);
        }
      });
  }

  loadApprovalStatsByClient() {
    this.productApiService.getApprovalStatsByClient()
      .subscribe({
        next: (stats) => {
          this.approvalStatsByClient = stats;
          this.isLoadingStats = false;
        },
        error: (error) => {
          console.error('Error loading approval stats by client:', error);
          this.isLoadingStats = false;
        }
      });
  }

  logout() {
    this.authService.removeToken();
    this.router.navigate(['/login']);
  }
}
