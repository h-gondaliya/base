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

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule, MatCardModule, MatButtonModule, MatInputModule, MatFormFieldModule, MatListModule, MatIconModule],
  templateUrl: './product-list.html',
  styleUrl: './product-list.scss'
})
export class ProductList {
  constructor(private router: Router) {
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
}
