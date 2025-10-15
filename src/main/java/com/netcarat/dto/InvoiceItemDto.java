package com.netcarat.dto;

import com.netcarat.modal.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO for individual invoice items/products based on SoldProducts entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDto {
    private Long productId;
    private BigDecimal soldPrice;
    private PaymentType paymentType;
    private String description;
}