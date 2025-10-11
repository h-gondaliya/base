package com.netcarat.controller;

import com.netcarat.modal.SoldProducts;
import com.netcarat.modal.PaymentType;
import com.netcarat.service.SoldProductsService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/sold-products")
@Tag(name = "Sold Products Management", description = "APIs for managing sold products")
public class SoldProductsController {

    @Autowired
    private SoldProductsService soldProductsService;

    @PostMapping("/create")
    @Operation(summary = "Create a new sold product", 
               description = "Creates a new sold product record with the provided parameters.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sold product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SoldProducts> createSoldProduct(
            @RequestParam Long productId,
            @RequestParam Long clientId,
            @RequestParam BigDecimal soldPrice,
            @RequestParam PaymentType paymentType,
            @RequestParam Long invoiceId) {
        
        try {
            SoldProducts soldProduct = soldProductsService.createSoldProduct(
                productId, clientId, soldPrice, paymentType, invoiceId);
            return ResponseEntity.status(HttpStatus.CREATED).body(soldProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all sold products", 
               description = "Retrieves all sold products from the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all sold products"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SoldProducts>> getAllSoldProducts() {
        try {
            List<SoldProducts> soldProducts = soldProductsService.getAllSoldProducts();
            return ResponseEntity.ok(soldProducts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get sold product by ID", 
               description = "Retrieves a specific sold product by its product ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved sold product"),
        @ApiResponse(responseCode = "404", description = "Sold product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SoldProducts> getSoldProductById(@PathVariable Long productId) {
        try {
            Optional<SoldProducts> soldProduct = soldProductsService.getSoldProductById(productId);
            return soldProduct.map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete sold product", 
               description = "Deletes a sold product by its product ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted sold product"),
        @ApiResponse(responseCode = "404", description = "Sold product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteSoldProduct(@PathVariable Long productId) {
        try {
            soldProductsService.deleteSoldProduct(productId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}