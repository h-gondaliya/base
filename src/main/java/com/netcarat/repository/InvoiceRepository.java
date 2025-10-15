package com.netcarat.repository;

import com.netcarat.modal.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.client WHERE i.activeFlag = true")
    List<Invoice> findAllActiveInvoices();
}