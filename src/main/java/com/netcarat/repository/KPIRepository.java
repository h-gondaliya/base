package com.netcarat.repository;


import com.netcarat.modal.KPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KPIRepository extends JpaRepository<KPI, UUID> {
    List<KPI> findByKpiDomain(String kpiDomain);
    List<KPI> findByKpiOwnerId(String kpiOwnerId);
    List<KPI> findByCreatedUserId(String createdUserId);
}
