package com.netcarat.service;

import com.netcarat.dto.ClientDto;
import com.netcarat.dto.InvoiceDto;
import com.netcarat.dto.InvoiceItemDto;
import com.netcarat.modal.Approval;
import com.netcarat.modal.Client;
import com.netcarat.modal.NetcaratStock;
import com.netcarat.repository.ApprovalRepository;
import com.netcarat.repository.ClientRepository;
import com.netcarat.repository.NetcaratStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for generating PDF invoices
 */
@Service
public class InvoiceService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ResourceLoader resourceLoader;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private NetcaratStockRepository stockRepository;
    
    @Autowired
    private ApprovalRepository approvalRepository;

    /**
     * Generate PDF invoice based on client ID and product list
     * 
     * @param clientId the client ID to generate invoice for
     * @param productIds the list of product IDs to include in the invoice
     * @return PDF invoice as byte array
     */
    public byte[] generateInvoiceByClientAndProducts(Long clientId, List<Long> productIds) {
        try {
            // Validate and fetch client
            Client client = validateAndFetchClient(clientId);
            
            // Validate products
            List<NetcaratStock> validProducts = validateProducts(productIds, clientId);
            
            // Create invoice data
            InvoiceDto invoiceData = createInvoiceData(client, validProducts);
            
            // Generate HTML from Thymeleaf template
            String htmlContent = generateHtmlContent(invoiceData);
            
            // Convert HTML to PDF
            return convertHtmlToPdf(htmlContent);
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating invoice for client ID: " + clientId, e);
        }
    }

    /**
     * Generate PDF invoice based on product ID
     * This is a demo implementation - in real scenario, you would fetch actual product data
     * 
     * @param productId the product ID to generate invoice for
     * @return PDF invoice as byte array
     */
    public byte[] generateInvoiceByProductId(String productId) {
        try {
            // Create demo invoice data based on product ID
            InvoiceDto invoiceData = createDemoInvoiceData(productId);
            
            // Generate HTML from Thymeleaf template
            String htmlContent = generateHtmlContent(invoiceData);
            
            // Convert HTML to PDF
            return convertHtmlToPdf(htmlContent);
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating invoice for product ID: " + productId, e);
        }
    }

    /**
     * Create demo invoice data for the given product ID
     * In a real implementation, this would fetch actual data from database
     */
    private InvoiceDto createDemoInvoiceData(String productId) {
        // Create demo client
        ClientDto client = new ClientDto(
            "Sample Client Corp",
            "123 Business Street, City, State 12345",
            "client@example.com",
            "+1 (555) 123-4567"
        );

        // Create demo invoice item based on product ID
        InvoiceItemDto item = new InvoiceItemDto(
            productId,
            "Product for ID: " + productId,
            "General",
            1,
            new BigDecimal("99.99")
        );

        List<InvoiceItemDto> items = Arrays.asList(item);

        // Generate invoice number with timestamp
        String invoiceNumber = "INV-" + productId + "-" + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Create invoice with 10% tax rate
        return new InvoiceDto(
            invoiceNumber,
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            client,
            items,
            new BigDecimal("19.0") // 10% tax rate
        );
    }

    /**
     * Generate HTML content from Thymeleaf template
     */
    private String generateHtmlContent(InvoiceDto invoiceData) {
        Context context = new Context();
        
        // Add all invoice data to template context
        context.setVariable("invoiceNumber", invoiceData.getInvoiceNumber());
        context.setVariable("invoiceDate", invoiceData.getInvoiceDate());
        context.setVariable("dueDate", invoiceData.getDueDate());
        context.setVariable("client", invoiceData.getClient());
        context.setVariable("invoiceItems", invoiceData.getInvoiceItems());
        context.setVariable("subtotal", invoiceData.getSubtotal());
        context.setVariable("taxRate", invoiceData.getTaxRate());
        context.setVariable("taxAmount", invoiceData.getTaxAmount());
        context.setVariable("totalAmount", invoiceData.getTotalAmount());

        return templateEngine.process("invoice", context);
    }

    /**
     * Convert HTML content to PDF using Flying Saucer
     */
    private byte[] convertHtmlToPdf(String htmlContent) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            
            // Set the HTML content
            renderer.setDocumentFromString(htmlContent);
            
            // Set base URL for resources (images, CSS, etc.)
            String baseUrl = resourceLoader.getResource("classpath:").getURI().toString();
            renderer.getSharedContext().setBaseURL(baseUrl);
            
            // Layout and render to PDF
            renderer.layout();
            renderer.createPDF(outputStream);
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IOException("Error converting HTML to PDF", e);
        }
    }

    /**
     * Validate and fetch client by ID
     */
    private Client validateAndFetchClient(Long clientId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new IllegalArgumentException("Client not found with ID: " + clientId);
        }
        return clientOpt.get();
    }

    /**
     * Validate products for availability and approval status
     */
    private List<NetcaratStock> validateProducts(List<Long> productIds, Long clientId) {
        // Check if all products exist
        List<NetcaratStock> products = stockRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            List<Long> foundIds = products.stream().map(NetcaratStock::getId).collect(Collectors.toList());
            List<Long> notFoundIds = productIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toList());
            throw new IllegalArgumentException("Products not found: " + notFoundIds);
        }

        // Check if any products are sold
        List<NetcaratStock> soldProducts = stockRepository.findSoldProductsByIdIn(productIds);
        if (!soldProducts.isEmpty()) {
            List<Long> soldIds = soldProducts.stream().map(NetcaratStock::getId).collect(Collectors.toList());
            throw new IllegalStateException("Products already sold: " + soldIds);
        }

        // Check if any products are approved for other clients
        List<Approval> approvals = approvalRepository.findApprovalsByProductIdsAndNotClient(productIds, clientId);
        if (!approvals.isEmpty()) {
            List<Long> approvedIds = approvals.stream()
                .map(a -> a.getProduct().getId())
                .distinct()
                .collect(Collectors.toList());
            throw new IllegalStateException("Products already approved for other clients: " + approvedIds);
        }

        return products;
    }

    /**
     * Create invoice data from client and products
     */
    private InvoiceDto createInvoiceData(Client client, List<NetcaratStock> products) {
        // Convert client to DTO
        ClientDto clientDto = new ClientDto(
            client.getClientName(),
            client.getClientAddress(),
            client.getEmail(),
            null // phone not available in Client entity
        );

        // Convert products to invoice items
        List<InvoiceItemDto> items = products.stream()
            .map(product -> new InvoiceItemDto(
                product.getId().toString(),
                product.getDescription() != null ? product.getDescription() : "Product " + product.getId(),
                product.getProductCategory() != null ? product.getProductCategory().toString() : "General",
                1, // quantity
                product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO
            ))
            .collect(Collectors.toList());

        // Generate invoice number with timestamp
        String invoiceNumber = "INV-" + client.getId() + "-" + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Create invoice with 10% tax rate
        return new InvoiceDto(
            invoiceNumber,
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            clientDto,
            items,
            new BigDecimal("19.0") // 19% tax rate
        );
    }
}