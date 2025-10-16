import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';
import {environment} from '../environments/environment';

export interface ClientApprovalStats {
  clientName: string;
  itemCount: number;
  totalPrice: number;
}

export interface AvailableProductCount {
  physicalStockCount: number;
  virtualStockCount: number;
}

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

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  /**
   * Get the count of available products
   * @returns Observable<number> - The count of available products
   */
  getAvailableProductsCount(): Observable<AvailableProductCount> {
    const headers = this.getAuthHeaders();
    return this.http.get<AvailableProductCount>(`${environment.apiUrl}/products/available/count`, { headers });
  }

  /**
   * Get approval statistics grouped by client
   * @returns Observable<ClientApprovalStats[]> - Array of client approval statistics
   */
  getApprovalStatsByClient(): Observable<ClientApprovalStats[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<ClientApprovalStats[]>(`${environment.apiUrl}/products/approval-stats-by-client`, { headers });
  }

  /**
   * Get invoice details by invoice number
   * @param invoiceNumber - The invoice number to retrieve details for
   * @returns Observable<InvoiceDto> - The invoice details
   */
  getInvoiceDetails(invoiceNumber: string): Observable<InvoiceDto> {
    const headers = this.getAuthHeaders();
    return this.http.get<InvoiceDto>(`${environment.apiUrl}/invoices/details/${invoiceNumber}`, { headers });
  }

  /**
   * Create authorization headers with JWT token
   * @returns HttpHeaders - Headers with Authorization token
   */
  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }
}
