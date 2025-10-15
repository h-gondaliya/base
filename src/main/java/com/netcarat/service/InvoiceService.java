package com.netcarat.service;

import com.netcarat.dto.CreateInvoiceRequestDto;
import com.netcarat.dto.InvoiceListDto;
import com.netcarat.dto.InvoiceDto;
import com.netcarat.dto.InvoiceItemDto;
import com.netcarat.dto.ClientDto;
import com.netcarat.modal.Invoice;
import com.netcarat.modal.InvoiceType;
import com.netcarat.modal.Client;
import com.netcarat.modal.NetcaratStock;
import com.netcarat.modal.SoldProducts;
import com.netcarat.modal.PaymentType;
import com.netcarat.repository.InvoiceRepository;
import com.netcarat.repository.ClientRepository;
import com.netcarat.repository.NetcaratStockRepository;
import com.netcarat.repository.SoldProductsRepository;
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
        handleProducts(products, client, savedInvoice, discount, invoiceType.equals(InvoiceType.APPROVAL));

        return savedInvoice;
    }
    
    private void handleProducts(List<NetcaratStock> products, Client client,
                                Invoice invoice, BigDecimal discount, boolean isApprovalType) {

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
            soldProduct.setPaymentType(isApprovalType? PaymentType.APPROVAL : PaymentType.CASH); // Default payment type
            soldProduct.setInvoice(invoice);
            
            soldProductsRepository.save(soldProduct);
        }
    }

    private void removeExistingApprovals(List<Long> productIds) {
        List<SoldProducts> existingApprovals = soldProductsRepository.findSoldProductsByProductIdIn(productIds);
        if (!existingApprovals.isEmpty()) {
            soldProductsRepository.deleteAll(existingApprovals);
        }
    }

    private String generateInvoiceNumber(Long clientId) {
        // Simple invoice number generation - could be enhanced
        return "INV-" + clientId + "-" +System.currentTimeMillis();
    }
    
    public List<InvoiceListDto> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAllActiveInvoices();
        return invoices.stream()
                .map(this::convertToInvoiceListDto)
                .collect(Collectors.toList());
    }
    
    private InvoiceListDto convertToInvoiceListDto(Invoice invoice) {
        List<SoldProducts> soldProducts = soldProductsRepository.findByInvoiceId(invoice.getId());
        
        Long totalItems = (long) soldProducts.size();
        BigDecimal totalPrice = soldProducts.stream()
                .map(SoldProducts::getSoldPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        String clientName = invoice.getClient() != null ? invoice.getClient().getClientName() : null;
        
        return new InvoiceListDto(
                invoice.getId(),
                invoice.getInvoiceType(),
                invoice.getInvoiceNumber(),
                totalItems,
                totalPrice,
                clientName
        );
    }
    
    /**
     * Get comprehensive invoice details by invoice number
     * Combines information from Invoice, Client, and SoldProducts using separate repository calls
     */
    public InvoiceDto getInvoiceDetailsByNumber(String invoiceNumber) {
        // Get invoice by invoice number
        Optional<Invoice> invoiceOpt = invoiceRepository.findByInvoiceNumber(invoiceNumber);
        if (invoiceOpt.isEmpty()) {
            throw new IllegalArgumentException("Invoice not found with number: " + invoiceNumber);
        }
        
        Invoice invoice = invoiceOpt.get();
        
        // Get client information
        Client client = invoice.getClient();
        ClientDto clientDto = new ClientDto(
                client.getClientName(),
                client.getClientAddress(), 
                client.getEmail(),
                null // phone not available in Client entity
        );
        
        // Get sold products for this invoice
        List<SoldProducts> soldProducts = soldProductsRepository.findByInvoiceId(invoice.getId());
        
        // Convert sold products to DTOs
        List<InvoiceItemDto> invoiceItems = soldProducts.stream()
                .map(soldProduct -> new InvoiceItemDto(
                        soldProduct.getProductId(),
                        soldProduct.getSoldPrice(),
                        soldProduct.getPaymentType(),
                        soldProduct.getDescription()
                ))
                .collect(Collectors.toList());
        
        // Calculate subtotal from sold products
        BigDecimal subtotal = soldProducts.stream()
                .map(SoldProducts::getSoldPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Create and return InvoiceDto
        return new InvoiceDto(
                invoice.getInvoiceNumber(),
                invoice.getInvoiceDate(),
                invoice.getDueDate(),
                clientDto,
                invoiceItems,
                invoice.getTax()
        );
    }
}