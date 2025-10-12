package com.netcarat.service;

import com.netcarat.dto.CreateInvoiceRequestDto;
import com.netcarat.modal.Invoice;
import com.netcarat.modal.InvoiceType;
import com.netcarat.modal.Client;
import com.netcarat.modal.NetcaratStock;
import com.netcarat.modal.SoldProducts;
import com.netcarat.modal.Approval;
import com.netcarat.modal.PaymentType;
import com.netcarat.repository.InvoiceRepository;
import com.netcarat.repository.ClientRepository;
import com.netcarat.repository.NetcaratStockRepository;
import com.netcarat.repository.SoldProductsRepository;
import com.netcarat.repository.ApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private NetcaratStockRepository netcaratStockRepository;

    @Autowired
    private SoldProductsRepository soldProductsRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private ProductService productService;

    @Transactional
    public Invoice createInvoice(CreateInvoiceRequestDto request) {
        
        // Extract parameters from DTO
        List<Long> productIds = request.getProductIds();
        Long clientId = request.getClientId();
        BigDecimal discount = request.getDiscount();
        String description = request.getDescription();
        InvoiceType invoiceType = request.getInvoiceType();
        BigDecimal tax = request.getTax();
        
        // Validate all products are available in virtualStockList
        List<NetcaratStock> physicallyAvailableStockItem = productService.findVirtuallyAvailableStockItems();
        Set<Long> availableProductIds = physicallyAvailableStockItem.stream()
                .map(NetcaratStock::getId)
                .collect(Collectors.toSet());
        
        for (Long productId : productIds) {
            if (!availableProductIds.contains(productId)) {
                throw new IllegalArgumentException("Product with id " + productId + " is not available in virtual stock");
            }
        }
        
        // Validate client exists
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new IllegalArgumentException("Client not found with id: " + clientId);
        }
        Client client = clientOpt.get();

        // Validate and fetch products
        List<NetcaratStock> products = netcaratStockRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("One or more products not found");
        }

        // Create new invoice
        Invoice invoice = new Invoice();
        
        // Set invoice fields
        invoice.setClient(client);
        invoice.setInvoiceType(invoiceType);
        invoice.setDiscount(discount != null ? discount : BigDecimal.ZERO);
        invoice.setDescription(description != null ? description : "");
        invoice.setTax(tax != null ? tax : BigDecimal.ZERO);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setCreatedBy("SYSTEM"); // Default value, could be updated based on authentication
        invoice.setInvoiceNumber(generateInvoiceNumber(client.getId()));
        
        // Save invoice first to get the ID
        Invoice savedInvoice = invoiceRepository.save(invoice);
        
        // Handle products based on invoice type
        if (invoiceType == InvoiceType.INVOICE) {
            // For Invoice type: calculate prices, apply discount, save to SoldProducts
            handleInvoiceTypeProducts(products, client, savedInvoice, discount);
        } else if (invoiceType == InvoiceType.APPROVAL) {
            // For approval type: save to approval table
            handleApprovalTypeProducts(products, client, savedInvoice);
        }
        
        return savedInvoice;
    }
    
    private void handleInvoiceTypeProducts(List<NetcaratStock> products, Client client, 
                                         Invoice invoice, BigDecimal discount) {
        // Remove existing approvals for these products
        List<Long> productIds = products.stream()
                .map(NetcaratStock::getId)
                .collect(Collectors.toList());
        removeExistingApprovals(productIds);

        for (NetcaratStock product : products) {
            // Calculate price with discount applied
            BigDecimal originalPrice = product.getPrice();
            BigDecimal discountAmount = BigDecimal.ZERO;
            if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
                discountAmount = originalPrice.multiply(discount).divide(new BigDecimal("100"));
            }
            BigDecimal finalPrice = originalPrice.subtract(discountAmount);
            
            // Create and save SoldProduct
            SoldProducts soldProduct = new SoldProducts();
            soldProduct.setProductId(product.getId());
            soldProduct.setClient(client);
            soldProduct.setSoldPrice(finalPrice);
            soldProduct.setPaymentType(PaymentType.CASH); // Default payment type
            soldProduct.setInvoice(invoice);
            
            soldProductsRepository.save(soldProduct);
        }
    }

    private void removeExistingApprovals(List<Long> productIds) {
        List<Approval> existingApprovals = approvalRepository.findByProductIdIn(productIds);
        if (!existingApprovals.isEmpty()) {
            approvalRepository.deleteAll(existingApprovals);
        }
    }

    private void handleApprovalTypeProducts(List<NetcaratStock> products, Client client, 
                                          Invoice invoice) {
        for (NetcaratStock product : products) {
            // Create and save Approval
            Approval approval = new Approval();
            approval.setClient(client);
            approval.setProduct(product);
            approval.setApprovalAmount(product.getPrice());
            approval.setApprovalDate(LocalDate.now());
            approval.setInvoice(invoice.getInvoiceNumber());
            
            approvalRepository.save(approval);
        }
    }

    private String generateInvoiceNumber(Long clientId) {
        // Simple invoice number generation - could be enhanced
        return "INV-" + clientId + "-" +System.currentTimeMillis();
    }
}