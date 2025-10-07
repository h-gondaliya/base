package com.netcarat.dto;

import java.util.List;
import java.util.UUID;

public class KPIDTO {
    private UUID id;

    private String createdUserId;

    private String kpiDomain;

    private String kpiOwnerId;

    private String kpiDescription;

    private String outcomeType;

    private String refreshFrequency;

    private List<String> sourceSystem;

    // Default constructor
    public KPIDTO() {}

    // Constructor with fields
    public KPIDTO(UUID id, String createdUserId, String kpiDomain, String kpiOwnerId,
                  String kpiDescription, String outcomeType, String refreshFrequency,
                  List<String> sourceSystem) {
        this.id = id;
        this.createdUserId = createdUserId;
        this.kpiDomain = kpiDomain;
        this.kpiOwnerId = kpiOwnerId;
        this.kpiDescription = kpiDescription;
        this.outcomeType = outcomeType;
        this.refreshFrequency = refreshFrequency;
        this.sourceSystem = sourceSystem;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getKpiDomain() {
        return kpiDomain;
    }

    public void setKpiDomain(String kpiDomain) {
        this.kpiDomain = kpiDomain;
    }

    public String getKpiOwnerId() {
        return kpiOwnerId;
    }

    public void setKpiOwnerId(String kpiOwnerId) {
        this.kpiOwnerId = kpiOwnerId;
    }

    public String getKpiDescription() {
        return kpiDescription;
    }

    public void setKpiDescription(String kpiDescription) {
        this.kpiDescription = kpiDescription;
    }

    public String getOutcomeType() {
        return outcomeType;
    }

    public void setOutcomeType(String outcomeType) {
        this.outcomeType = outcomeType;
    }

    public String getRefreshFrequency() {
        return refreshFrequency;
    }

    public void setRefreshFrequency(String refreshFrequency) {
        this.refreshFrequency = refreshFrequency;
    }

    public List<String> getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(List<String> sourceSystem) {
        this.sourceSystem = sourceSystem;
    }
}
