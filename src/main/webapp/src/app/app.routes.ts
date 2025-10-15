import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent) },
  { path: 'product-list', loadComponent: () => import('./product-list/product-list').then(m => m.ProductList) },
  { path: 'invoice-list', loadComponent: () => import('./invoice-list/invoice-list.component').then(m => m.InvoiceListComponent) },
  { path: 'invoice/:invoiceNumber', loadComponent: () => import('./invoice/invoice').then(m => m.Invoice) },
  { path: 'invoice-confirmation', loadComponent: () => import('./invoice-confirmation/invoice-confirmation.component').then(m => m.InvoiceConfirmationComponent) },
  { path: '**', redirectTo: '/login' }
];
