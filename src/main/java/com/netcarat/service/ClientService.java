package com.netcarat.service;

import com.netcarat.modal.Client;
import com.netcarat.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Get all clients with their ID and name
     * @return List of maps containing client ID and client name
     */
    public List<Map<String, Object>> getAllClientsIdAndName() {
        List<Client> clients = clientRepository.findAll();
        
        return clients.stream()
                .map(client -> {
                    Map<String, Object> clientInfo = new HashMap<>();
                    clientInfo.put("id", client.getId());
                    clientInfo.put("clientName", client.getClientName());
                    return clientInfo;
                })
                .collect(Collectors.toList());
    }
}