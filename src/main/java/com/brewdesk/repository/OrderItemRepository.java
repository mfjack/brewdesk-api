package com.brewdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brewdesk.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
}
