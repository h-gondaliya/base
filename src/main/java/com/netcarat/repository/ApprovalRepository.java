package com.netcarat.repository;

import com.netcarat.modal.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    
    @Query("SELECT COUNT(DISTINCT a.product.id) FROM Approval a")
    long countDistinctApprovedProducts();
    
    @Query("SELECT a.client.clientName, COUNT(a), SUM(a.approvalAmount) FROM Approval a GROUP BY a.client.clientName")
    List<Object[]> getApprovalStatsByClient();
}