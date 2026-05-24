package com.suranjan.mas.product.service;

import com.suranjan.mas.product.dto.ProductRequest;
import com.suranjan.mas.product.dto.ProductResponse;
import com.suranjan.mas.product.entity.Product;
import com.suranjan.mas.exception.ProductNotFoundException;
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
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());

        Product savedProduct = repository.save(product);

        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getCategory(),
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
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product updateProduct(Long id, Product product) {

        Product existingProduct = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setName(product.getName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setImageUrl(product.getImageUrl());

        return repository.save(existingProduct);
    }

    public void deleteProduct(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        repository.delete(product);
    }

    public List<Product> getProductByCategory(String category) {
        return repository.findByCategory(category);
    }

    public List<Product> getProductByName(String name) {
        return repository.findByNameContaining(name);
    }

    public Page<Product> getProductsWithPaginationAndSorting(
            int page,
            int size,
            String field,
            String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(field).ascending()
                : Sort.by(field).descending();

        return repository.findAll(PageRequest.of(page, size, sort));
    }

    public List<Product> getProductsByPriceRange(
            double minPrice,
            double maxPrice
    ) {
        return repository.findByPriceBetween(minPrice, maxPrice);
    }

    public Page<Product> getProductsAdvanced(
            String category,
            String name,
            Double minPrice,
            Double maxPrice,
            int page,
            int size,
            String sortField,
            String direction
    ) {
        Specification<Product> spec =
                (root, query, cb) -> cb.conjunction();

        if (category != null && !category.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category"), category));
        }

        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("name"), "%" + name + "%"));
        }

        if (minPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return repository.findAll(spec, pageable);
    }
}