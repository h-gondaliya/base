package com.netcarat.modal;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "approval")
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private NetcaratStock product;

    @Column(name = "approval_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal approvalAmount;

    @Column(name = "approval_date", nullable = false)
    private LocalDate approvalDate;

    @Column(name = "invoice", length = 50)
    private String invoice;

    // Default constructor
    public Approval() {}

    // Constructor with all fields
    public Approval(Client client, NetcaratStock product, BigDecimal approvalAmount, 
                   LocalDate approvalDate, String invoice) {
        this.client = client;
        this.product = product;
        this.approvalAmount = approvalAmount;
        this.approvalDate = approvalDate;
        this.invoice = invoice;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public NetcaratStock getProduct() {
        return product;
    }

    public void setProduct(NetcaratStock product) {
        this.product = product;
    }

    public BigDecimal getApprovalAmount() {
        return approvalAmount;
    }

    public void setApprovalAmount(BigDecimal approvalAmount) {
        this.approvalAmount = approvalAmount;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}