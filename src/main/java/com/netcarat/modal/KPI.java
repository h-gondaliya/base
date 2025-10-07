package com.netcarat.modal;


import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "kpis")
public class KPI {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String createdUserId;

    @Column(nullable = false)
    private String kpiDomain;

    @Column(nullable = false)
    private String kpiOwnerId;

    @Column(nullable = false, length = 1000)
    private String kpiDescription;

    @Column(nullable = false)
    private String outcomeType;

    @Column(nullable = false)
    private String refreshFrequency;

    @ElementCollection
    @CollectionTable(name = "kpi_source_systems", joinColumns = @JoinColumn(name = "kpi_id"))
    @Column(name = "source_system")
    private List<String> sourceSystem;

    // Default constructor
    public KPI() {}

    // Constructor with fields
    public KPI(String createdUserId, String kpiDomain, String kpiOwnerId,
               String kpiDescription, String outcomeType, String refreshFrequency,
               List<String> sourceSystem) {
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
