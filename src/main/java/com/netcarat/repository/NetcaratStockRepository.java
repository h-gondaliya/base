package com.netcarat.repository;

import com.netcarat.modal.NetcaratStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NetcaratStockRepository extends JpaRepository<NetcaratStock, Long> {

    @Query("SELECT COUNT(n) FROM NetcaratStock n WHERE n.soldPrice IS NULL AND n.paymentType IS NULL")
    long countUnsoldItems();
    
    @Query("SELECT n.productCategory, COUNT(n) FROM NetcaratStock n GROUP BY n.productCategory")
    List<Object[]> countByProductCategory();

    @Query("SELECT n FROM NetcaratStock n WHERE n.id IN :productIds AND (n.soldPrice IS NOT NULL OR n.paymentType IS NOT NULL)")
    List<NetcaratStock> findSoldProductsByIdIn(@Param("productIds") List<Long> productIds);
}