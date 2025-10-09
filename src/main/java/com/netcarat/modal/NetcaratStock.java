package com.netcarat.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "netcarat_stock")
public class NetcaratStock {

    // Getters and Setters
    @Id
    private Long id;

    @Column(name = "gross_weight", precision = 10, scale = 2)
    private BigDecimal grossWeight;

    @Column(name = "diamond_pieces")
    private Integer diamondPieces;

    @Column(name = "diamond_weight", precision = 10, scale = 2)
    private BigDecimal diamondWeight;

    @Enumerated(EnumType.STRING)
    @Column(name = "gold_kt", length = 10)
    private GoldKt goldKt;

    @Column(name = "colour_stone_weight", precision = 10, scale = 2)
    private BigDecimal colourStoneWeight;

    @Column(name = "colour_stone_type", length = 50)
    private String colourStoneType;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", length = 10)
    private ProductCategory productCategory;

    @Column(name = "design_number", length = 50)
    private String designNumber;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "sold_price", precision = 10, scale = 2)
    private BigDecimal soldPrice;

    @Column(name = "payment_type", length = 20)
    private String paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "sell_date")
    private LocalDate sellDate;

    @Column(name = "description", length = 500)
    private String description;

    // Default constructor
    public NetcaratStock() {}

}