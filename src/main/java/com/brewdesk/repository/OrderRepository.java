package com.brewdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brewdesk.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
