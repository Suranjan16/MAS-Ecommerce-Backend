package com.suranjan.mas.product.service;

import com.suranjan.mas.product.dto.ProductRequest;
import com.suranjan.mas.product.dto.ProductResponse;
import com.suranjan.mas.product.entity.Product;
import com.suranjan.mas.product.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductResponse addProduct(ProductRequest request) {
        Product product = new Product();

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setSubCategory(request.getSubCategory());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());

        Product savedProduct = repository.save(product);

        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getCategory(),
                savedProduct.getSubCategory(),
                savedProduct.getPrice(),
                savedProduct.getQuantity(),
                savedProduct.getImageUrl()
        );
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProduct(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProduct(id);

        existingProduct.setName(product.getName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setSubCategory(product.getSubCategory());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setImageUrl(product.getImageUrl());

        return repository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product product = getProduct(id);

        repository.delete(product);
    }

    public List<Product> getProductByCategory(String category) {
        return repository.findByCategory(category);
    }

    public List<Product> getProductByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public Page<Product> getProductsWithPaginationAndSorting(
            int page,
            int size,
            String sort,
            String direction
    ) {
        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortDirection, sort)
                );

        return repository.findAll(pageable);
    }

    public List<Product> getProductsByPriceRange(
            double minPrice,
            double maxPrice
    ) {
        return repository.findByPriceBetween(
                minPrice,
                maxPrice
        );
    }

    public Page<Product> getProductsAdvanced(
            String category,
            String subCategory,
            String name,
            Double minPrice,
            Double maxPrice,
            int page,
            int size,
            String sort,
            String direction
    ) {
        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortDirection, sort)
                );

        Specification<Product> specification =
                (root, query, cb) -> cb.conjunction();

        if (category != null && !category.isBlank()) {
            specification = specification.and(
                    (root, query, cb) ->
                            cb.equal(root.get("category"), category)
            );
        }

        if (subCategory != null && !subCategory.isBlank()) {
            specification = specification.and(
                    (root, query, cb) ->
                            cb.equal(root.get("subCategory"), subCategory)
            );
        }

        if (name != null && !name.isBlank()) {
            specification = specification.and(
                    (root, query, cb) ->
                            cb.like(
                                    cb.lower(root.get("name")),
                                    "%" + name.toLowerCase() + "%"
                            )
            );
        }

        if (minPrice != null) {
            specification = specification.and(
                    (root, query, cb) ->
                            cb.greaterThanOrEqualTo(
                                    root.get("price"),
                                    minPrice
                            )
            );
        }

        if (maxPrice != null) {
            specification = specification.and(
                    (root, query, cb) ->
                            cb.lessThanOrEqualTo(
                                    root.get("price"),
                                    maxPrice
                            )
            );
        }

        return repository.findAll(
                specification,
                pageable
        );
    }
}