import { Component } from '@angular/core';
import {Router} from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialogModule, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ZXingScannerModule } from '@zxing/ngx-scanner';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule, MatCardModule, MatButtonModule, MatInputModule, MatFormFieldModule, MatListModule, MatIconModule, MatTooltipModule, MatDialogModule, ZXingScannerModule],
  templateUrl: './product-list.html',
  styleUrl: './product-list.scss'
})
export class ProductList {
  constructor(private router: Router, private dialog: MatDialog) {
  }

  // Product list management
  productId: string = '';
  productList: string[] = [];

  // Product list management methods
  addProduct() {
    const trimmedProductId = this.productId.trim();

    if (trimmedProductId === '') {
      return; // Don't add empty product IDs
    }

    // Check for uniqueness
    if (this.productList.includes(trimmedProductId)) {
      return; // Don't add duplicate product IDs
    }

    this.productList.push(trimmedProductId);
    this.productId = ''; // Clear the input field
  }

  removeProduct(productId: string) {
    const index = this.productList.indexOf(productId);
    if (index > -1) {
      this.productList.splice(index, 1);
    }
  }

  navigateToDashboard() {
    this.router.navigate(['/dashboard']);
  }

  createInvoice() {
    // TODO: Implement invoice creation logic
    console.log('Creating invoice for products:', this.productList);
  }

  createApproval() {
    // TODO: Implement approval creation logic
    console.log('Creating approval for products:', this.productList);
  }

  openQRScanner() {
    const dialogRef = this.dialog.open(QRScannerDialogComponent, {
      width: '400px',
      height: '500px',
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.trim()) {
        const scannedProductId = result.trim();

        // Check if product already exists
        if (!this.productList.includes(scannedProductId)) {
          this.productList.push(scannedProductId);
        }
      }
    });
  }
}

@Component({
  selector: 'qr-scanner-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatIconModule, ZXingScannerModule],
  template: `
    <div class="scanner-dialog">
      <div class="scanner-header">
        <h3>Scan QR Code</h3>
        <button mat-icon-button (click)="closeDialog()" class="close-btn">
          <mat-icon>close</mat-icon>
        </button>
      </div>
      <div class="scanner-content">
        <zxing-scanner
          (scanSuccess)="onScanSuccess($event)"
          (scanError)="onScanError($event)">
        </zxing-scanner>
      </div>
    </div>
  `,
  styles: [`
    .scanner-dialog {
      padding: 0;
    }
    .scanner-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px;
      border-bottom: 1px solid #e0e0e0;
    }
    .scanner-content {
      height: 400px;
      width: 100%;
    }
    .close-btn {
      margin-left: auto;
    }
  `]
})
export class QRScannerDialogComponent {
  constructor(private dialogRef: MatDialogRef<QRScannerDialogComponent>) {}

  onScanSuccess(result: string) {
    this.dialogRef.close(result);
  }

  onScanError(error: any) {
    console.error('Scan error:', error);
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
