package com.netcarat.controller;

import com.netcarat.dto.InvoiceGenerationRequestDto;
import com.netcarat.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for invoice generation and management
 */
@RestController
@RequestMapping("/api/invoices")
@Tag(name = "Invoice Management", description = "APIs for generating and managing invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/generate")
    @Operation(summary = "Generate invoice by client and products", 
               description = "Generates a PDF invoice for the specified client ID and product list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully generated PDF invoice"),
        @ApiResponse(responseCode = "400", description = "Invalid client ID or product list"),
        @ApiResponse(responseCode = "404", description = "Client not found or products not available"),
        @ApiResponse(responseCode = "409", description = "Some products are already sold or approved for other clients"),
        @ApiResponse(responseCode = "500", description = "Internal server error during PDF generation")
    })
    public ResponseEntity<byte[]> generateInvoice(
            @Parameter(description = "Invoice generation request with client ID and product list", required = true)
            @RequestBody InvoiceGenerationRequestDto request) {
        
        try {
            // Validate request
            if (request == null || request.getClientId() == null || 
                request.getProductIds() == null || request.getProductIds().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Generate PDF invoice
            byte[] pdfContent = invoiceService.generateInvoiceByClientAndProducts(
                request.getClientId(), request.getProductIds());

            // Prepare response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "invoice-client-" + request.getClientId() + ".pdf");
            headers.setContentLength(pdfContent.length);

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            // Client not found or validation errors
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            // Products already sold or approved for other clients
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            // Log the error (in real application, use proper logging framework)
            System.err.println("Error generating invoice: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/generate/{productId}")
    @Operation(summary = "Generate invoice by product ID", 
               description = "Generates a PDF invoice for the specified product ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully generated PDF invoice"),
        @ApiResponse(responseCode = "400", description = "Invalid product ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error during PDF generation")
    })
    public ResponseEntity<byte[]> generateInvoiceByProductId(
            @Parameter(description = "Product ID to generate invoice for", required = true, example = "PROD-001")
            @PathVariable String productId) {
        
        try {
            // Validate product ID
            if (productId == null || productId.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Generate PDF invoice
            byte[] pdfContent = invoiceService.generateInvoiceByProductId(productId);

            // Prepare response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "invoice-" + productId + ".pdf");
            headers.setContentLength(pdfContent.length);

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            // Log the error (in real application, use proper logging framework)
            System.err.println("Error generating invoice for product ID: " + productId + ", Error: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/generate/{productId}/preview")
    @Operation(summary = "Preview invoice HTML by product ID", 
               description = "Generates HTML preview of invoice for the specified product ID (for debugging)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully generated HTML preview"),
        @ApiResponse(responseCode = "400", description = "Invalid product ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> previewInvoiceByProductId(
            @Parameter(description = "Product ID to preview invoice for", required = true, example = "PROD-001")
            @PathVariable String productId) {
        
        try {
            // Validate product ID
            if (productId == null || productId.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // This would require adding a preview method to InvoiceService
            // For now, return a simple message
            String htmlContent = "<html><body><h1>Invoice Preview for Product: " + productId + "</h1>" +
                "<p>This is a preview endpoint. To implement full preview, add a previewInvoice method to InvoiceService.</p>" +
                "</body></html>";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_HTML);

            return new ResponseEntity<>(htmlContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("Error previewing invoice for product ID: " + productId + ", Error: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}