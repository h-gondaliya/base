import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth.service';

export interface InvoiceListDto {
  id: number;
  invoiceType: string;
  invoiceNumber: string;
  totalItems: number;
  totalPrice: number;
  clientName: string;
}

@Component({
  selector: 'app-invoice-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './invoice-list.component.html',
  styleUrls: ['./invoice-list.component.scss']
})
export class InvoiceListComponent implements OnInit {
  invoiceList: InvoiceListDto[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';
  displayedColumns: string[] = ['invoiceNumber', 'invoiceType', 'clientName', 'totalItems', 'totalPrice'];

  constructor(
    private router: Router,
    private http: HttpClient,
    private authService: AuthService
  ) {}

  ngOnInit() {
    // Check authentication
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    this.loadInvoices();
  }

  loadInvoices() {
    this.isLoading = true;
    this.errorMessage = '';

    const token = this.authService.getToken();
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    this.http.get<InvoiceListDto[]>('/api/invoices/list', { headers })
      .subscribe({
        next: (invoices) => {
          this.invoiceList = invoices;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading invoices:', error);
          this.errorMessage = 'Failed to load invoices. Please try again.';
          this.isLoading = false;
        }
      });
  }

  navigateToDashboard() {
    this.router.navigate(['/dashboard']);
  }

  getTotalValue(): number {
    return this.invoiceList.reduce((total, invoice) => total + invoice.totalPrice, 0);
  }
}
