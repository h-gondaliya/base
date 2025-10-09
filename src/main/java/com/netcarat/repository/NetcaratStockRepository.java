package com.netcarat.repository;

import com.netcarat.modal.NetcaratStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetcaratStockRepository extends JpaRepository<NetcaratStock, Long> {

    @Query("SELECT COUNT(n) FROM NetcaratStock n WHERE n.soldPrice IS NULL AND n.paymentType IS NULL")
    long countUnsoldItems();
    
    @Query("SELECT n.productCategory, COUNT(n) FROM NetcaratStock n GROUP BY n.productCategory")
    List<Object[]> countByProductCategory();
}