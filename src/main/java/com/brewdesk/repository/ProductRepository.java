package com.brewdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brewdesk.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
}
