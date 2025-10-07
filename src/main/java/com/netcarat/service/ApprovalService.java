package com.netcarat.service;

import com.netcarat.modal.Approval;
import com.netcarat.modal.Client;
import com.netcarat.modal.NetcaratStock;
import com.netcarat.repository.ApprovalRepository;
import com.netcarat.repository.ClientRepository;
import com.netcarat.repository.NetcaratStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final ClientRepository clientRepository;
    private final NetcaratStockRepository netcaratStockRepository;

    @Autowired
    public ApprovalService(ApprovalRepository approvalRepository,
                          ClientRepository clientRepository,
                          NetcaratStockRepository netcaratStockRepository) {
        this.approvalRepository = approvalRepository;
        this.clientRepository = clientRepository;
        this.netcaratStockRepository = netcaratStockRepository;
    }

    @Transactional
    public List<Approval> importFromCsv(InputStream csvInputStream) throws IOException {
        List<Approval> approvals = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                
                String[] fields = line.split(",");
                if (fields.length < 5 || line.trim().isEmpty()) {
                    continue; // Skip empty or incomplete lines
                }
                
                try {
                    // Parse and validate fields
                    String clientIdStr = fields[0].trim();
                    String productIdStr = fields[1].trim();
                    String approvalAmountStr = fields[2].replace("\"", "").replace(",", "").trim();
                    String approvalDateStr = fields[3].replace("\"", "").trim();
                    String invoice = fields[4].trim();
                    
                    // Skip invalid entries
                    if (clientIdStr.isEmpty() || productIdStr.isEmpty() || approvalAmountStr.isEmpty()) {
                        continue;
                    }
                    
                    // Skip non-numeric product IDs (like "Pisa Tower")
                    if (!productIdStr.matches("\\d+")) {
                        continue;
                    }
                    
                    Long clientId = Long.parseLong(clientIdStr);
                    Long productId = Long.parseLong(productIdStr);
                    BigDecimal approvalAmount = new BigDecimal(approvalAmountStr);
                    LocalDate approvalDate = LocalDate.parse(approvalDateStr, formatter);
                    
                    // Check if client and product exist
                    Optional<Client> clientOpt = clientRepository.findById(clientId);
                    Optional<NetcaratStock> productOpt = netcaratStockRepository.findById(productId);
                    
                    if (clientOpt.isPresent() && productOpt.isPresent()) {
                        Approval approval = new Approval(
                            clientOpt.get(),
                            productOpt.get(),
                            approvalAmount,
                            approvalDate,
                            invoice
                        );
                        approvals.add(approval);
                    } else {
                        System.out.println("Skipping record - Client ID: " + clientId + 
                                         ", Product ID: " + productId + " (not found in database)");
                    }
                    
                } catch (NumberFormatException | DateTimeParseException e) {
                    System.out.println("Skipping invalid record: " + line + " - Error: " + e.getMessage());
                }
            }
        }
        
        return approvalRepository.saveAll(approvals);
    }
    
    public List<Approval> getAllApprovals() {
        return approvalRepository.findAll();
    }
    
    public Optional<Approval> getApprovalById(Long id) {
        return approvalRepository.findById(id);
    }
    
    public Approval saveApproval(Approval approval) {
        return approvalRepository.save(approval);
    }
    
    public void deleteApproval(Long id) {
        approvalRepository.deleteById(id);
    }
}