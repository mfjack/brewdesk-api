package com.brewdesk.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.brewdesk.domain.Category;
import com.brewdesk.domain.Product;
import com.brewdesk.dto.category.CategoryResponse;
import com.brewdesk.dto.product.ProductRequest;
import com.brewdesk.dto.product.ProductResponse;
import com.brewdesk.repository.CategoryRepository;
import com.brewdesk.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponse create(ProductRequest productRequest) {
        if(productRepository.existsByName(productRequest.name())) {
            throw new IllegalArgumentException("Product with the same name already exists");
        }

        Product product = new Product();
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());

        Category category = categoryRepository.findById(productRequest.categoryId())
        .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + productRequest.categoryId()));

        product.setCategory(category);

        productRepository.save(product);
        return new ProductResponse(
            product.getId(), 
            product.getName(), 
            product.getDescription(), 
            product.getPrice(), 
            new CategoryResponse(
                product.getCategory().getId(), 
                product.getCategory().getName()
            )
        );
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
            .map(product -> new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                new CategoryResponse(
                    product.getCategory().getId(),
                    product.getCategory().getName()
                )
            ))
            .toList();        
    }

    

    public ProductResponse update(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());

        Category category = categoryRepository.findById(productRequest.categoryId())
        .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + productRequest.categoryId()));

        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return new ProductResponse(
            updatedProduct.getId(), 
            updatedProduct.getName(), 
            updatedProduct.getDescription(), 
            updatedProduct.getPrice(), 
            new CategoryResponse(
                updatedProduct.getCategory().getId(), 
                updatedProduct.getCategory().getName()
            )
        );
    }

    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        return new ProductResponse(
            product.getId(), 
            product.getName(), 
            product.getDescription(), 
            product.getPrice(), 
            new CategoryResponse(
                product.getCategory().getId(), 
                product.getCategory().getName()
            )
        );
    }

    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        productRepository.delete(product);
    }
}
