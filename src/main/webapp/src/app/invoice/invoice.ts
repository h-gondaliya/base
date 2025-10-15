import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth.service';

export interface ClientDto {
  name: string;
  address: string;
  email: string;
  phone: string;
}

export interface InvoiceItemDto {
  productId: number;
  soldPrice: number;
  paymentType: string;
  description: string;
}

export interface InvoiceDto {
  invoiceNumber: string;
  invoiceDate: string;
  dueDate: string;
  client: ClientDto;
  invoiceItems: InvoiceItemDto[];
  subtotal: number;
  taxRate: number;
  taxAmount: number;
  totalAmount: number;
}

@Component({
  selector: 'app-invoice',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './invoice.html',
  styleUrl: './invoice.scss'
})
export class Invoice implements OnInit {
  invoiceData: InvoiceDto | null = null;
  isLoading: boolean = false;
  errorMessage: string = '';
  invoiceNumber: string = '';

  constructor(
    private route: ActivatedRoute,
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

    // Get invoice number from route parameters
    this.invoiceNumber = this.route.snapshot.paramMap.get('invoiceNumber') || '';

    if (this.invoiceNumber) {
      this.loadInvoiceDetails();
    } else {
      this.errorMessage = 'Invalid invoice number';
    }
  }

  loadInvoiceDetails() {
    this.isLoading = true;
    this.errorMessage = '';

    const token = this.authService.getToken();
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    this.http.get<InvoiceDto>(`/api/invoices/details/${this.invoiceNumber}`, { headers })
      .subscribe({
        next: (invoice) => {
          this.invoiceData = invoice;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading invoice details:', error);
          this.errorMessage = 'Failed to load invoice details. Please try again.';
          this.isLoading = false;
        }
      });
  }

  navigateBack() {
    this.router.navigate(['/invoice-list']);
  }

  printInvoice() {
    window.print();
  }
}
