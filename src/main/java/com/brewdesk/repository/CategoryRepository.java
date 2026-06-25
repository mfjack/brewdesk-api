package com.brewdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brewdesk.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
