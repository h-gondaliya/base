package com.netcarat.repository;

import com.netcarat.modal.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    
    @Query("SELECT COUNT(DISTINCT a.product.id) FROM Approval a")
    long countDistinctApprovedProducts();
}