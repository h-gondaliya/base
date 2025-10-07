package com.netcarat.controller;

import com.netcarat.dto.KPIDTO;
import com.netcarat.service.KPIService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/kpis/")
public class KPIController {

    private final KPIService kpiService;

    @Autowired
    public KPIController(KPIService kpiService) {
        this.kpiService = kpiService;
    }

    // Create a new KPI
    @PostMapping
    public ResponseEntity<KPIDTO> createKPI(@RequestBody KPIDTO kpiDTO) {
        KPIDTO createdKPI = kpiService.createKPI(kpiDTO);
        return new ResponseEntity<>(createdKPI, HttpStatus.CREATED);
    }

    // Get all KPIs
    @GetMapping
    public ResponseEntity<List<KPIDTO>> getAllKPIs() {
        List<KPIDTO> kpis = kpiService.getAllKPIs();
        return new ResponseEntity<>(kpis, HttpStatus.OK);
    }

    // Get KPI by ID
    @GetMapping("/{id}")
    public ResponseEntity<KPIDTO> getKPIById(@PathVariable UUID id) {
        try {
            KPIDTO kpi = kpiService.getKPIById(id);
            return new ResponseEntity<>(kpi, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update KPI
    @PutMapping("/{id}")
    public ResponseEntity<KPIDTO> updateKPI(@PathVariable UUID id, @RequestBody KPIDTO kpiDTO) {
        try {
            KPIDTO updatedKPI = kpiService.updateKPI(id, kpiDTO);
            return new ResponseEntity<>(updatedKPI, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete KPI
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKPI(@PathVariable UUID id) {
        try {
            kpiService.deleteKPI(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get KPIs by domain
    @GetMapping("/domain/{domain}")
    public ResponseEntity<List<KPIDTO>> getKPIsByDomain(@PathVariable String domain) {
        List<KPIDTO> kpis = kpiService.getKPIsByDomain(domain);
        return new ResponseEntity<>(kpis, HttpStatus.OK);
    }

    // Get KPIs by owner ID
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<KPIDTO>> getKPIsByOwnerId(@PathVariable String ownerId) {
        List<KPIDTO> kpis = kpiService.getKPIsByOwnerId(ownerId);
        return new ResponseEntity<>(kpis, HttpStatus.OK);
    }

    // Get KPIs by created user ID
    @GetMapping("/creator/{userId}")
    public ResponseEntity<List<KPIDTO>> getKPIsByCreatedUserId(@PathVariable String userId) {
        List<KPIDTO> kpis = kpiService.getKPIsByCreatedUserId(userId);
        return new ResponseEntity<>(kpis, HttpStatus.OK);
    }
}
