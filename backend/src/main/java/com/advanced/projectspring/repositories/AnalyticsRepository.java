package com.advanced.projectspring.repositories;

import com.advanced.projectspring.models.Order;
import com.advanced.projectspring.models.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<Order, Long> {

    // Last N orders across the entire platform
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findRecentOrders(Pageable pageable);

    // Revenue per store (for admin dashboard)
    @Query("SELECT o.store.id, o.store.name, SUM(o.grandTotal) FROM Order o GROUP BY o.store.id, o.store.name ORDER BY SUM(o.grandTotal) DESC")
    List<Object[]> findRevenueByStore();

    // Count of orders grouped by status
    @Query("SELECT o.status, COUNT(o.id) FROM Order o GROUP BY o.status")
    List<Object[]> findOrderCountByStatus();

    // Best-selling products (by units ordered)
    @Query("SELECT oi.product.id, oi.product.name, SUM(oi.quantity) as totalQty FROM OrderItem oi GROUP BY oi.product.id, oi.product.name ORDER BY totalQty DESC")
    List<Object[]> findBestSellingProducts(Pageable pageable);

    // Most expensive products
    @Query("SELECT p FROM Product p ORDER BY p.unitPrice DESC")
    List<Product> findMostExpensiveProducts(Pageable pageable);

    // Total revenue (all time)
    @Query("SELECT SUM(o.grandTotal) FROM Order o")
    Double findTotalRevenue();

    // Total order count
    @Query("SELECT COUNT(o) FROM Order o")
    Long findTotalOrderCount();

    // Orders this month
    @Query("SELECT COUNT(o) FROM Order o WHERE MONTH(o.createdAt) = MONTH(CURRENT_DATE) AND YEAR(o.createdAt) = YEAR(CURRENT_DATE)")
    Long findOrdersThisMonth();
}
