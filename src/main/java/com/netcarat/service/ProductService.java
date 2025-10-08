package com.netcarat.service;

import com.netcarat.repository.ApprovalRepository;
import com.netcarat.repository.NetcaratStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}