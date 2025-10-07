package com.netcarat.modal;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "netcarat_stock")
public class NetcaratStock {

    @Id
    private Long id;

    @Column(name = "gross_weight", precision = 10, scale = 2)
    private BigDecimal grossWeight;

    @Column(name = "diamond_pieces")
    private Integer diamondPieces;

    @Column(name = "diamond_weight", precision = 10, scale = 2)
    private BigDecimal diamondWeight;

    @Column(name = "gold_kt", length = 10)
    private String goldKt;

    @Column(name = "colour_stone_weight", precision = 10, scale = 2)
    private BigDecimal colourStoneWeight;

    @Column(name = "colour_stone_type", length = 50)
    private String colourStoneType;

    @Column(name = "product_catagory", length = 10)
    private String productCatagory;

    @Column(name = "design_number", length = 50)
    private String designNumber;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "sold_price", precision = 10, scale = 2)
    private BigDecimal soldPrice;

    @Column(name = "payment_type", length = 20)
    private String paymentType;

    @Column(name = "client_id")
    private Integer clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private Client client;

    @Column(name = "sell_date")
    private LocalDate sellDate;

    @Column(name = "description", length = 500)
    private String description;

    // Default constructor
    public NetcaratStock() {}

    // Constructor with all fields
    public NetcaratStock(Long id, BigDecimal grossWeight, Integer diamondPieces, 
                        BigDecimal diamondWeight, String goldKt, BigDecimal colourStoneWeight,
                        String colourStoneType, String productCatagory, String designNumber,
                        BigDecimal price, BigDecimal soldPrice, String paymentType,
                        Integer clientId, LocalDate sellDate, String description) {
        this.id = id;
        this.grossWeight = grossWeight;
        this.diamondPieces = diamondPieces;
        this.diamondWeight = diamondWeight;
        this.goldKt = goldKt;
        this.colourStoneWeight = colourStoneWeight;
        this.colourStoneType = colourStoneType;
        this.productCatagory = productCatagory;
        this.designNumber = designNumber;
        this.price = price;
        this.soldPrice = soldPrice;
        this.paymentType = paymentType;
        this.clientId = clientId;
        this.sellDate = sellDate;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(BigDecimal grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Integer getDiamondPieces() {
        return diamondPieces;
    }

    public void setDiamondPieces(Integer diamondPieces) {
        this.diamondPieces = diamondPieces;
    }

    public BigDecimal getDiamondWeight() {
        return diamondWeight;
    }

    public void setDiamondWeight(BigDecimal diamondWeight) {
        this.diamondWeight = diamondWeight;
    }

    public String getGoldKt() {
        return goldKt;
    }

    public void setGoldKt(String goldKt) {
        this.goldKt = goldKt;
    }

    public BigDecimal getColourStoneWeight() {
        return colourStoneWeight;
    }

    public void setColourStoneWeight(BigDecimal colourStoneWeight) {
        this.colourStoneWeight = colourStoneWeight;
    }

    public String getColourStoneType() {
        return colourStoneType;
    }

    public void setColourStoneType(String colourStoneType) {
        this.colourStoneType = colourStoneType;
    }

    public String getProductCatagory() {
        return productCatagory;
    }

    public void setProductCatagory(String productCatagory) {
        this.productCatagory = productCatagory;
    }

    public String getDesignNumber() {
        return designNumber;
    }

    public void setDesignNumber(String designNumber) {
        this.designNumber = designNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(BigDecimal soldPrice) {
        this.soldPrice = soldPrice;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public LocalDate getSellDate() {
        return sellDate;
    }

    public void setSellDate(LocalDate sellDate) {
        this.sellDate = sellDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}