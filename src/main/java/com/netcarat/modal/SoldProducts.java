package com.netcarat.modal;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sold_products")
public class SoldProducts {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "sold_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal soldPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    // Default constructor
    public SoldProducts() {}

    // Constructor with all fields
    public SoldProducts(Long productId, Client client, BigDecimal soldPrice, 
                       PaymentType paymentType, Invoice invoice) {
        this.productId = productId;
        this.client = client;
        this.soldPrice = soldPrice;
        this.paymentType = paymentType;
        this.invoice = invoice;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BigDecimal getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(BigDecimal soldPrice) {
        this.soldPrice = soldPrice;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}