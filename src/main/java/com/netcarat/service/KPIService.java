package com.netcarat.service;

import com.netcarat.dto.KPIDTO;
import com.netcarat.modal.KPI;
import com.netcarat.repository.KPIRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class KPIService {

    private final KPIRepository kpiRepository;

    @Autowired
    public KPIService(KPIRepository kpiRepository) {
        this.kpiRepository = kpiRepository;
    }

    // Convert Entity to DTO
    private KPIDTO convertToDTO(KPI kpi) {
        return new KPIDTO(
                kpi.getId(),
                kpi.getCreatedUserId(),
                kpi.getKpiDomain(),
                kpi.getKpiOwnerId(),
                kpi.getKpiDescription(),
                kpi.getOutcomeType(),
                kpi.getRefreshFrequency(),
                kpi.getSourceSystem()
        );
    }

    // Convert DTO to Entity
    private KPI convertToEntity(KPIDTO kpiDTO) {
        KPI kpi = new KPI();
        if (kpiDTO.getId() != null) {
            kpi.setId(kpiDTO.getId());
        }
        kpi.setCreatedUserId(kpiDTO.getCreatedUserId());
        kpi.setKpiDomain(kpiDTO.getKpiDomain());
        kpi.setKpiOwnerId(kpiDTO.getKpiOwnerId());
        kpi.setKpiDescription(kpiDTO.getKpiDescription());
        kpi.setOutcomeType(kpiDTO.getOutcomeType());
        kpi.setRefreshFrequency(kpiDTO.getRefreshFrequency());
        kpi.setSourceSystem(kpiDTO.getSourceSystem());
        return kpi;
    }

    // Create a new KPI
    public KPIDTO createKPI(KPIDTO kpiDTO) {
        KPI kpi = convertToEntity(kpiDTO);
        KPI savedKPI = kpiRepository.save(kpi);
        return convertToDTO(savedKPI);
    }

    // Get all KPIs
    public List<KPIDTO> getAllKPIs() {
        return kpiRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get KPI by ID
    public KPIDTO getKPIById(UUID id) {
        Optional<KPI> kpiOptional = kpiRepository.findById(id);
        if (kpiOptional.isPresent()) {
            return convertToDTO(kpiOptional.get());
        } else {
            throw new EntityNotFoundException("KPI not found with ID: " + id);
        }
    }

    // Update KPI
    public KPIDTO updateKPI(UUID id, KPIDTO kpiDTO) {
        if (!kpiRepository.existsById(id)) {
            throw new EntityNotFoundException("KPI not found with ID: " + id);
        }

        kpiDTO.setId(id); // Ensure ID is set
        KPI kpi = convertToEntity(kpiDTO);
        KPI updatedKPI = kpiRepository.save(kpi);
        return convertToDTO(updatedKPI);
    }

    // Delete KPI
    public void deleteKPI(UUID id) {
        if (!kpiRepository.existsById(id)) {
            throw new EntityNotFoundException("KPI not found with ID: " + id);
        }
        kpiRepository.deleteById(id);
    }

    // Find KPIs by domain
    public List<KPIDTO> getKPIsByDomain(String domain) {
        return kpiRepository.findByKpiDomain(domain)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Find KPIs by owner ID
    public List<KPIDTO> getKPIsByOwnerId(String ownerId) {
        return kpiRepository.findByKpiOwnerId(ownerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Find KPIs by created user ID
    public List<KPIDTO> getKPIsByCreatedUserId(String userId) {
        return kpiRepository.findByCreatedUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}