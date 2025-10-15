package com.netcarat.controller;

import com.netcarat.dto.CreateInvoiceRequestDto;
import com.netcarat.dto.InvoiceListDto;
import com.netcarat.dto.InvoiceDto;
import com.netcarat.modal.Invoice;
import com.netcarat.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@Tag(name = "Invoice Management", description = "APIs for managing invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/create")
    @Operation(summary = "Create a new invoice", 
               description = "Creates a new invoice with the provided parameters. Optional fields will use default values if not provided.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Invoice created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Invoice> createInvoice(@RequestBody CreateInvoiceRequestDto request) {
        
        try {
            Invoice invoice = invoiceService.createInvoice(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/list")
    @Operation(summary = "Get all invoices", 
               description = "Retrieves a list of all invoices with invoice type, invoice number, total items in sold_product table, total price, and client name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved invoice list"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<InvoiceListDto>> getAllInvoices() {
        try {
            List<InvoiceListDto> invoices = invoiceService.getAllInvoices();
            return ResponseEntity.ok(invoices);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/details/{invoiceNumber}")
    @Operation(summary = "Get invoice details by invoice number", 
               description = "Retrieves comprehensive invoice information including invoice number, invoice date, client details, due date, list of products with all sold_products fields, tax, and calculated subtotal")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved invoice details"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<InvoiceDto> getInvoiceDetails(@PathVariable String invoiceNumber) {
        try {
            InvoiceDto invoiceDetails = invoiceService.getInvoiceDetailsByNumber(invoiceNumber);
            return ResponseEntity.ok(invoiceDetails);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}