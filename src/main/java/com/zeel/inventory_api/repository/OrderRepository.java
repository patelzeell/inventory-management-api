package com.zeel.inventory_api.repository;


import com.zeel.inventory_api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByStatus(Order.OrderStatus status, Pageable pageable);
}