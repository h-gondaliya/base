package com.netcarat.dto;

import com.netcarat.modal.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for create invoice request containing all required parameters
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceRequestDto {
    private List<Long> productIds;
    private Long clientId;
    private BigDecimal discount;
    private String description;
    private InvoiceType invoiceType;
    private BigDecimal tax;
}