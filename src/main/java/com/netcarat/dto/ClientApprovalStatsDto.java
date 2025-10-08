package com.netcarat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ClientApprovalStatsDto {
    
    private String clientName;
    private Long itemCount;
    private BigDecimal totalPrice;
    

    public ClientApprovalStatsDto(String clientName, Long itemCount, BigDecimal totalPrice) {
        this.clientName = clientName;
        this.itemCount = itemCount;
        this.totalPrice = totalPrice;
    }

}