package com.netcarat.dto;

import com.netcarat.modal.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceListDto {
    
    private Long invoiceId;
    private InvoiceType invoiceType;
    private String invoiceNumber;
    private Long totalItemsInSoldProduct;
    private BigDecimal totalPrice;
    private String clientName;
}