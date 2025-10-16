package com.netcarat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netcarat.modal.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for complete invoice information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {
    private String invoiceNumber;
    private InvoiceType invoiceType;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate invoiceDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dueDate;
    private ClientDto client;
    private List<InvoiceItemDto> invoiceItems;
    private BigDecimal subtotal;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;

    /**
     * Constructor that calculates totals automatically
     */
    public InvoiceDto(String invoiceNumber, InvoiceType invoiceType, LocalDate invoiceDate, LocalDate dueDate, 
                      ClientDto client, List<InvoiceItemDto> invoiceItems, BigDecimal taxRate) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceType = invoiceType;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.client = client;
        this.invoiceItems = invoiceItems;
        this.taxRate = taxRate;
        
        // Calculate totals
        calculateTotals();
    }

    /**
     * Calculate subtotal, tax amount, and total amount
     */
    private void calculateTotals() {
        this.subtotal = invoiceItems.stream()
                .map(InvoiceItemDto::getSoldPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.taxAmount = subtotal.multiply(taxRate.divide(new BigDecimal(100)));
        this.totalAmount = subtotal.add(taxAmount);
    }

    /**
     * Recalculate totals when items are modified
     */
    public void recalculateTotals() {
        calculateTotals();
    }
}