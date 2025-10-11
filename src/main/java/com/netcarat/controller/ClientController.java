package com.netcarat.controller;

import com.netcarat.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client Management", description = "APIs for managing clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/list")
    @Operation(summary = "Get all clients", 
               description = "Retrieves a list of all clients with their ID and name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved client list"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Map<String, Object>>> getAllClients() {
        try {
            List<Map<String, Object>> clients = clientService.getAllClientsIdAndName();
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}