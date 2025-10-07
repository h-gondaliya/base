package com.netcarat.service;

import com.netcarat.modal.Client;
import com.netcarat.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class ClientDataLoader implements CommandLineRunner {

    @Autowired
    private ClientRepository repository;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (repository.count() > 0) {
            System.out.println("Client data already exists. Skipping client data loading.");
            return;
        }

//        loadClientData();
    }

    private void loadClientData() {
        System.out.println("Loading client data from CSV...");

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("client.csv");
            if (inputStream == null) {
                System.err.println("client.csv file not found in resources");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> csvData = new ArrayList<>();
            String line;
            
            // Skip header line
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    csvData.add(line);
                }
            }
            reader.close();

            saveClientsFromCSV(csvData);

        } catch (Exception e) {
            System.err.println("Error loading client data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveClientsFromCSV(List<String> csvData) {
        System.out.println("Processing " + csvData.size() + " client records...");

        int loadedCount = 0;
        for (String line : csvData) {
            try {
                Client client = parseCSVLine(line);
                if (client != null) {
                    repository.save(client);
                    loadedCount++;
                }
            } catch (Exception e) {
                System.err.println("Error parsing line: " + line + " - " + e.getMessage());
            }
        }

        System.out.println("Loaded " + loadedCount + " Client records.");
    }

    private Client parseCSVLine(String line) {
        // Handle multi-line CSV entries by reconstructing the line properly
        String[] fields = parseCSVFields(line);
        
        if (fields.length < 4) {
            return null;
        }

        Client client = new Client();
        
        // Parse ID - handle empty ID case
        String idStr = fields[0].trim();
        if (idStr.isEmpty()) {
            return null; // Skip entries without ID
        }
        client.setId(Long.parseLong(idStr));
        
        // Parse client name
        client.setClientName(fields[1].trim().isEmpty() ? null : fields[1].trim());
        
        // Parse client address
        client.setClientAddress(fields[2].trim().isEmpty() ? null : fields[2].trim());
        
        // Parse email
        client.setEmail(fields[3].trim().isEmpty() ? null : fields[3].trim());
        
        return client;
    }

    private String[] parseCSVFields(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        
        // Add the last field
        fields.add(currentField.toString());
        
        return fields.toArray(new String[0]);
    }
}