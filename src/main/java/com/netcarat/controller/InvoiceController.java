package com.netcarat.controller;

import com.netcarat.modal.Invoice;
import com.netcarat.modal.InvoiceType;
import com.netcarat.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public ResponseEntity<Invoice> createInvoice(
            @RequestParam List<Long> productIds,
            @RequestParam Long clientId,
            @RequestParam(required = false) BigDecimal discount,
            @RequestParam(required = false) String description,
            @RequestParam InvoiceType invoiceType,
            @RequestParam(required = false) BigDecimal tax) {
        
        try {
            Invoice invoice = invoiceService.createInvoice(productIds, clientId, discount, 
                                                         description, invoiceType, tax);
            return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}