package com.netcarat.repository;

import com.netcarat.modal.NetcaratStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetcaratStockRepository extends JpaRepository<NetcaratStock, Long> {

    @Query("SELECT COUNT(n) FROM NetcaratStock n WHERE n.paymentType IS NULL AND n.soldPrice IS NULL AND n.id NOT IN (SELECT sp.productId FROM SoldProducts sp)")
    long countUnsoldItems();

    @Query("SELECT COUNT(n) FROM NetcaratStock n WHERE n.paymentType IS NULL AND n.soldPrice IS NULL AND n.id NOT IN (SELECT sp.productId FROM SoldProducts sp WHERE sp.paymentType != 'APPROVAL')")
    long countAvailableItems();

    @Query("SELECT n.productCategory, COUNT(n) FROM NetcaratStock n GROUP BY n.productCategory")
    List<Object[]> countByProductCategory();

    @Query("SELECT n FROM NetcaratStock n WHERE n.paymentType IS NULL AND n.soldPrice IS NULL AND n.id NOT IN (SELECT sp.productId FROM SoldProducts sp where sp.paymentType != 'APPROVAL')")
    List<NetcaratStock> findVirtuallyAvailableStockItems();

    @Query("SELECT n FROM NetcaratStock n WHERE n.paymentType IS NULL AND n.soldPrice IS NULL AND n.id NOT IN (SELECT sp.productId FROM SoldProducts sp WHERE sp.paymentType != 'APPROVAL')")
    List<NetcaratStock> findPhysicallyAvailableStockItem();
}