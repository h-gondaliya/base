package com.netcarat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO for invoice generation request containing client ID and product list
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceGenerationRequestDto {
    private Long clientId;
    private List<Long> productIds;
}