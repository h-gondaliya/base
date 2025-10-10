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
import { MatDialogModule, MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Inject } from '@angular/core';
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
      height: '600px',
      disableClose: true,
      data: { productList: this.productList }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.scannedProducts && result.scannedProducts.length > 0) {
        // Add all scanned products to the list
        result.scannedProducts.forEach((productId: string) => {
          if (!this.productList.includes(productId)) {
            this.productList.push(productId);
          }
        });
      }
    });
  }
}

@Component({
  selector: 'qr-scanner-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatIconModule, ZXingScannerModule, CommonModule],
  template: `
    <div class="scanner-dialog">
      <div class="scanner-header">
        <h3>Continuous QR Scanner</h3>
        <button mat-icon-button (click)="closeDialog()" class="close-btn">
          <mat-icon>close</mat-icon>
        </button>
      </div>
      <div class="scanner-content">
        <zxing-scanner
          (scanSuccess)="onScanSuccess($event)"
          (scanError)="onScanError($event)">
        </zxing-scanner>

        <div class="scan-feedback" *ngIf="lastScannedProduct">
          <div class="success-message">
            ✓ Scanned: {{ lastScannedProduct }}
          </div>
        </div>

        <div class="scanned-list" *ngIf="scannedProducts.length > 0">
          <h4>Scanned Products ({{ scannedProducts.length }})</h4>
          <div class="product-item" *ngFor="let product of scannedProducts">
            {{ product }}
          </div>
        </div>
      </div>

      <div class="scanner-footer">
        <button mat-raised-button color="warn" (click)="cancelScanning()" class="cancel-btn">
          Cancel
        </button>
        <button mat-raised-button color="primary" (click)="finishScanning()" class="finish-btn" [disabled]="scannedProducts.length === 0">
          Finish ({{ scannedProducts.length }})
        </button>
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
      background: #f5f5f5;
    }
    .scanner-content {
      height: 450px;
      width: 100%;
      position: relative;
    }
    .scanner-footer {
      display: flex;
      justify-content: space-between;
      gap: 12px;
      padding: 16px;
      border-top: 1px solid #e0e0e0;
      background: #f5f5f5;
    }
    .scan-feedback {
      position: absolute;
      top: 10px;
      left: 10px;
      right: 10px;
      z-index: 10;
    }
    .success-message {
      background: #4CAF50;
      color: white;
      padding: 8px 12px;
      border-radius: 4px;
      font-weight: 500;
      animation: fadeInOut 2s ease-in-out;
    }
    .scanned-list {
      position: absolute;
      bottom: 10px;
      left: 10px;
      right: 10px;
      background: rgba(255, 255, 255, 0.95);
      border-radius: 8px;
      padding: 12px;
      max-height: 120px;
      overflow-y: auto;
      border: 1px solid #ddd;
    }
    .scanned-list h4 {
      margin: 0 0 8px 0;
      font-size: 14px;
      color: #333;
    }
    .product-item {
      padding: 4px 8px;
      margin: 2px 0;
      background: #e8f5e8;
      border-radius: 4px;
      font-size: 12px;
      border-left: 3px solid #4CAF50;
    }
    .cancel-btn {
      flex: 1;
    }
    .finish-btn {
      flex: 1;
    }
    .close-btn {
      margin-left: auto;
    }
    @keyframes fadeInOut {
      0% { opacity: 0; transform: translateY(-10px); }
      20% { opacity: 1; transform: translateY(0); }
      80% { opacity: 1; transform: translateY(0); }
      100% { opacity: 0; transform: translateY(-10px); }
    }
  `]
})
export class QRScannerDialogComponent {
  scannedProducts: string[] = [];
  lastScannedProduct: string | null = null;
  existingProducts: string[] = [];

  constructor(
    private dialogRef: MatDialogRef<QRScannerDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.existingProducts = data?.productList || [];
  }

  onScanSuccess(result: string) {
    const scannedProductId = result.trim();

    if (scannedProductId && !this.scannedProducts.includes(scannedProductId)) {
      this.scannedProducts.push(scannedProductId);
      this.lastScannedProduct = scannedProductId;

      // Clear the feedback message after 2 seconds
      setTimeout(() => {
        this.lastScannedProduct = null;
      }, 2000);
    }
  }

  onScanError(error: any) {
    console.error('Scan error:', error);
  }

  cancelScanning() {
    this.dialogRef.close(null);
  }

  finishScanning() {
    this.dialogRef.close({ scannedProducts: this.scannedProducts });
  }

  closeDialog() {
    this.dialogRef.close(null);
  }
}
