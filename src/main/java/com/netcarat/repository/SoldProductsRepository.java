package com.netcarat.repository;

import com.netcarat.modal.PaymentType;
import com.netcarat.modal.SoldProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SoldProductsRepository extends JpaRepository<SoldProducts, Long> {
    List<SoldProducts> findSoldProductsByProductIdIn(Collection<Long> productIds);

    List<SoldProducts> findByPaymentType(PaymentType paymentType);
    
    List<SoldProducts> findByInvoiceId(Long invoiceId);
}