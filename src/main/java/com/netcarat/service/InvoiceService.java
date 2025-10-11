package com.netcarat.service;

import com.netcarat.modal.Invoice;
import com.netcarat.modal.InvoiceType;
import com.netcarat.modal.Client;
import com.netcarat.modal.NetcaratStock;
import com.netcarat.repository.InvoiceRepository;
import com.netcarat.repository.ClientRepository;
import com.netcarat.repository.NetcaratStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private NetcaratStockRepository netcaratStockRepository;

    @Transactional
    public Invoice createInvoice(List<Long> productIds, Long clientId, BigDecimal discount, 
                               String description, InvoiceType invoiceType, BigDecimal tax) {
        
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
        
        // Set required fields
        invoice.setProducts(products);
        invoice.setClient(client);
        invoice.setInvoiceType(invoiceType);
        
        // Set optional fields with defaults
        invoice.setDiscount(discount != null ? discount : BigDecimal.ZERO);
        invoice.setDescription(description != null ? description : "");
        invoice.setTax(tax != null ? tax : BigDecimal.ZERO);
        
        // Set default values for other required fields
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setCreatedBy("SYSTEM"); // Default value, could be updated based on authentication
        invoice.setInvoiceNumber(generateInvoiceNumber());
        
        return invoiceRepository.save(invoice);
    }

    private String generateInvoiceNumber() {
        // Simple invoice number generation - could be enhanced
        return "INV-" + System.currentTimeMillis();
    }
}