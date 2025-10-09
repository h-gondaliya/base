package com.netcarat.controller;

import com.netcarat.dto.ClientApprovalStatsDto;
import com.netcarat.modal.ProductCategory;
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
import java.util.List;
import java.util.Map;

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

    @GetMapping("/approval-stats-by-client")
    @Operation(summary = "Get approval statistics by client", 
               description = "Returns approval statistics grouped by client including client name, item count, and total price")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved approval statistics by client"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ClientApprovalStatsDto>> getApprovalStatsByClient() {
        try {
            List<ClientApprovalStatsDto> stats = productService.getApprovalStatsByClient();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/count-by-category")
    @Operation(summary = "Get product count by category", 
               description = "Returns the count of products grouped by product category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved product count by category"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<ProductCategory, Long>> getProductCountByCategory() {
        try {
            Map<ProductCategory, Long> countByCategory = productService.getProductCountByCategory();
            return ResponseEntity.ok(countByCategory);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}