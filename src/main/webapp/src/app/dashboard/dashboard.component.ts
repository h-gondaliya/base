import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { ApiService, ClientApprovalStats } from '../services/api.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatTableModule, MatProgressSpinnerModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  username: string = '';
  token: string = '';
  physicalCount: number = 0;
  virtualCount: number = 0;
  approvalStatsByClient: ClientApprovalStats[] = [];
  isLoadingStats: boolean = false;
  displayedColumns: string[] = ['clientName', 'itemCount', 'totalPrice'];

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
          this.physicalCount = count.physicalStockCount;
          this.virtualCount = count.virtualStockCount;
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

  // Calculate total item count across all clients
  get totalItemCount(): number {
    return this.approvalStatsByClient.reduce((total, stat) => total + stat.itemCount, 0);
  }

  // Calculate total price across all clients
  get totalPrice(): number {
    return this.approvalStatsByClient.reduce((total, stat) => total + stat.totalPrice, 0);
  }

  // Navigate to product list page
  navigateToProductList() {
    this.router.navigate(['/product-list']);
  }
}
