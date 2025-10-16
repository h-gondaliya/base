import { Component, OnInit, Pipe, PipeTransform } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from '../auth.service';
import {ApiService, InvoiceDto, InvoiceType} from '../services/api.service';

@Pipe({
  name: 'replaceLineBreaks',
  standalone: true
})
export class ReplaceLineBreaksPipe implements PipeTransform {
  transform(value: string): string {
    if (!value) return value;
    return value.replace(/\n/g, '<br>');
  }
}

@Component({
  selector: 'app-invoice',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    NgOptimizedImage,
    ReplaceLineBreaksPipe
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
    private apiService: ApiService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

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

    this.apiService.getInvoiceDetails(this.invoiceNumber)
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

  protected readonly InvoiceType = InvoiceType;
}
