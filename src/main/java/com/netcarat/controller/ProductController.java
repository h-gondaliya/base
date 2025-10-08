package com.netcarat.controller;

import com.netcarat.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "APIs for managing products and inventory")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/available/count")
    @Operation(summary = "Get available products count", 
               description = "Returns the count of available products (Total stock items - Approved items)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved available products count"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> getAvailableProductsCount() {
        try {
            long availableCount = productService.getAvailableProductsCount();
            return ResponseEntity.ok(availableCount);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}