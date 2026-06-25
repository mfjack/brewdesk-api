package com.brewdesk.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.brewdesk.domain.Category;
import com.brewdesk.dto.category.CategoryRequest;
import com.brewdesk.dto.category.CategoryResponse;
import com.brewdesk.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponse create(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.name())) {
            throw new IllegalArgumentException("Category with the same name already exists");
        }

        Category category = new Category();
        category.setName(categoryRequest.name());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(savedCategory.getId(), savedCategory.getName());
    }

    public List<CategoryResponse> findAll() {
       return categoryRepository.findAll().stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .toList();
    }

    public CategoryResponse deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        categoryRepository.delete(category);

        return new CategoryResponse(category.getId(), category.getName());

    }
 }
