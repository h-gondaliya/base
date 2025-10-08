package com.netcarat.service;

import com.netcarat.dto.ClientApprovalStatsDto;
import com.netcarat.repository.ApprovalRepository;
import com.netcarat.repository.NetcaratStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private NetcaratStockRepository stockRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    /**
     * Get the count of available products
     * Available products = Total items in stock table - Approval items
     * 
     * @return count of available products
     */
    public long getAvailableProductsCount() {
        long totalStockCount = stockRepository.countUnsoldItems();
        long approvedProductsCount = approvalRepository.countDistinctApprovedProducts();
        return totalStockCount - approvedProductsCount;
    }

    /**
     * Get the count of unsold items
     * Unsold items are items where sold_price and payment_type are not available (null)
     * 
     * @return count of unsold items
     */
    public long getTotalUnsoldItemsCount() {
        return stockRepository.countUnsoldItems();
    }

    /**
     * Get the total count of items in approval table per client and also total price
     * 
     * @return list of client approval statistics containing client info, item count, and total price
     */
    public List<ClientApprovalStatsDto> getApprovalStatsByClient() {
        List<Object[]> results = approvalRepository.getApprovalStatsByClient();
        
        return results.stream()
            .map(row -> new ClientApprovalStatsDto(
                (String) row[0],         // client name
                (Long) row[1],           // item count
                (BigDecimal) row[2]      // total price
            ))
            .collect(Collectors.toList());
    }
}