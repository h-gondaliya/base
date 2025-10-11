package com.netcarat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockCountDto {
    private Long physicalStockCount;
    private Long virtualStockCount;
}