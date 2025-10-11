package com.netcarat.service;

import com.netcarat.modal.SoldProducts;
import com.netcarat.modal.Client;
import com.netcarat.modal.Invoice;
import com.netcarat.modal.PaymentType;
import com.netcarat.repository.SoldProductsRepository;
import com.netcarat.repository.ClientRepository;
import com.netcarat.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SoldProductsService {

    @Autowired
    private SoldProductsRepository soldProductsRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Transactional
    public SoldProducts createSoldProduct(Long productId, Long clientId, BigDecimal soldPrice, 
                                        PaymentType paymentType, Long invoiceId) {
        
        // Validate client exists
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new IllegalArgumentException("Client not found with id: " + clientId);
        }
        Client client = clientOpt.get();

        // Validate invoice exists
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if (invoiceOpt.isEmpty()) {
            throw new IllegalArgumentException("Invoice not found with id: " + invoiceId);
        }
        Invoice invoice = invoiceOpt.get();

        // Create new sold product
        SoldProducts soldProduct = new SoldProducts();
        soldProduct.setProductId(productId);
        soldProduct.setClient(client);
        soldProduct.setSoldPrice(soldPrice);
        soldProduct.setPaymentType(paymentType);
        soldProduct.setInvoice(invoice);
        
        return soldProductsRepository.save(soldProduct);
    }

    public List<SoldProducts> getAllSoldProducts() {
        return soldProductsRepository.findAll();
    }

    public Optional<SoldProducts> getSoldProductById(Long productId) {
        return soldProductsRepository.findById(productId);
    }

    @Transactional
    public SoldProducts updateSoldProduct(SoldProducts soldProduct) {
        return soldProductsRepository.save(soldProduct);
    }

    @Transactional
    public void deleteSoldProduct(Long productId) {
        soldProductsRepository.deleteById(productId);
    }
}