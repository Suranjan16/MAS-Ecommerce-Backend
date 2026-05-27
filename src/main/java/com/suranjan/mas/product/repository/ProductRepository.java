package com.suranjan.mas.product.repository;

import com.suranjan.mas.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    List<Product> findByCategory(String category);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceBetween(
            Double minPrice,
            Double maxPrice
    );
}