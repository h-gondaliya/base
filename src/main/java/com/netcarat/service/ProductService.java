package com.netcarat.service;

import com.netcarat.dto.ClientApprovalStatsDto;
import com.netcarat.modal.NetcaratStock;
import com.netcarat.modal.PaymentType;
import com.netcarat.modal.ProductCategory;
import com.netcarat.modal.SoldProducts;
import com.netcarat.repository.NetcaratStockRepository;
import com.netcarat.repository.SoldProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private NetcaratStockRepository stockRepository;

    @Autowired
    private SoldProductsRepository soldProductsRepository;

    /**
     * Get the count of available products
     * Physically Available products = Total items in stock table - Approval items
     * @return count of available products
     */
    public long getPhysicalCount() {
        return stockRepository.countAvailableItems();
    }

    /**
     * Total stock count (Physical + Approval)
     * @return count of available products
     */
    public long getVirtualCount() {
        return stockRepository.countUnsoldItems();
    }


    public List<NetcaratStock> findVirtuallyAvailableStockItems() {
        return stockRepository.findVirtuallyAvailableStockItems();
    }
    /**
     * Get the total count of items in approval table per client and also total price
     * 
     * @return list of client approval statistics containing client info, item count, and total price
     */
    public List<ClientApprovalStatsDto> getApprovalStatsByClient() {
        List<SoldProducts> results = soldProductsRepository.findByPaymentType(PaymentType.APPROVAL);
        if (results != null && !results.isEmpty()) {
            // Group by client and aggregate statistics
            Map<String, ClientApprovalStatsDto> clientStatsMap = new HashMap<>();
            
            for (SoldProducts soldProduct : results) {
                String clientName = soldProduct.getClient().getClientName();
                
                if (clientStatsMap.containsKey(clientName)) {
                    // Update existing stats
                    ClientApprovalStatsDto stats = clientStatsMap.get(clientName);
                    stats.setItemCount(stats.getItemCount() + 1);
                    stats.setTotalPrice(stats.getTotalPrice().add(soldProduct.getSoldPrice()));
                } else {
                    // Create new stats entry
                    clientStatsMap.put(clientName, new ClientApprovalStatsDto(
                        clientName, 
                        1L, 
                        soldProduct.getSoldPrice()
                    ));
                }
            }
            
            return clientStatsMap.values().stream().collect(java.util.stream.Collectors.toList());
        }
        return java.util.Collections.emptyList();
    }

    /**
     * Get the count of products grouped by product category
     * 
     * @return map containing product category as key and count as value
     */
    public Map<ProductCategory, Long> getProductCountByCategory() {
        List<Object[]> results = stockRepository.countByProductCategory();
        Map<ProductCategory, Long> categoryCountMap = new HashMap<>();
        
        for (Object[] result : results) {
            ProductCategory category = (ProductCategory) result[0];
            Long count = (Long) result[1];
            
            // Skip records with null category to avoid ClassCastException
            if (category != null) {
                categoryCountMap.put(category, count);
            }
        }
        
        return categoryCountMap;
    }
}