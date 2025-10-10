package com.netcarat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO for individual invoice items/products
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDto {
    private String productId;
    private String description;
    private String category;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    /**
     * Constructor that calculates total price automatically
     */
    public InvoiceItemDto(String productId, String description, String category, Integer quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(new BigDecimal(quantity));
    }
}