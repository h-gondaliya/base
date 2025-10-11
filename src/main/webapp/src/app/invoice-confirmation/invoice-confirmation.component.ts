import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

interface Client {
  id: number;
  clientName: string;
}

@Component({
  selector: 'app-invoice-confirmation',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatRadioModule,
    MatSelectModule,
    MatFormFieldModule,
    MatIconModule,
    MatListModule,
    MatSnackBarModule
  ],
  templateUrl: './invoice-confirmation.component.html',
  styleUrls: ['./invoice-confirmation.component.css']
})
export class InvoiceConfirmationComponent implements OnInit {
  productList: string[] = [];
  selectedType: string = 'INVOICE';
  selectedClientId: number | null = null;
  clients: Client[] = [];
  isLoading = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    // Get product list from query parameters
    this.route.queryParams.subscribe(params => {
      if (params['products']) {
        this.productList = JSON.parse(params['products']);
      } else {
        // If no products, redirect back to product list
        this.router.navigate(['/product-list']);
      }
    });

    // Load clients
    this.loadClients();
  }

  loadClients() {
    this.isLoading = true;
    this.http.get<Client[]>('/api/clients/list').subscribe({
      next: (clients) => {
        this.clients = clients;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading clients:', error);
        this.snackBar.open('Error loading clients', 'Close', { duration: 3000 });
        this.isLoading = false;
      }
    });
  }

  onConfirm() {
    if (!this.selectedClientId) {
      this.snackBar.open('Please select a client', 'Close', { duration: 3000 });
      return;
    }

    if (this.productList.length === 0) {
      this.snackBar.open('No products to process', 'Close', { duration: 3000 });
      return;
    }

    this.isLoading = true;

    // Convert product IDs to numbers (assuming they are numeric)
    const productIds = this.productList.map(id => parseInt(id, 10)).filter(id => !isNaN(id));

    if (productIds.length === 0) {
      this.snackBar.open('Invalid product IDs', 'Close', { duration: 3000 });
      this.isLoading = false;
      return;
    }

    // Prepare request body as JSON for the API call
    const requestBody = {
      productIds: productIds,
      clientId: this.selectedClientId!,
      discount: null,
      description: null,
      invoiceType: this.selectedType,
      tax: null
    };

    this.http.post('/api/invoices/create', requestBody).subscribe({
      next: (response) => {
        this.snackBar.open(`${this.selectedType.toLowerCase()} created successfully!`, 'Close', { duration: 3000 });
        this.router.navigate(['/dashboard']);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error creating invoice:', error);
        this.snackBar.open(`Error creating ${this.selectedType.toLowerCase()}`, 'Close', { duration: 3000 });
        this.isLoading = false;
      }
    });
  }

  onBack() {
    this.router.navigate(['/product-list']);
  }
}
