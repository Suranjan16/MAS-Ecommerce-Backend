package com.suranjan.mas.product.controller;

import com.suranjan.mas.product.dto.ProductRequest;
import com.suranjan.mas.product.dto.ProductResponse;
import com.suranjan.mas.product.entity.Product;
import com.suranjan.mas.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ProductResponse addProduct(@RequestBody ProductRequest request) {
        return service.addProduct(request);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") Long id) {
        return service.getProduct(id);
    }

    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody Product product
    ) {
        return service.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        service.deleteProduct(id);

        return "Product Deleted Successfully";
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductByCategory(
            @PathVariable("category") String category
    ) {
        return service.getProductByCategory(category);
    }

    @GetMapping("/search/{name}")
    public List<Product> getProductByName(
            @PathVariable("name") String name
    ) {
        return service.getProductByName(name);
    }

    @GetMapping("/page")
    public Page<Product> getProducts(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort,
            @RequestParam String direction
    ) {
        return service.getProductsWithPaginationAndSorting(
                page,
                size,
                sort,
                direction
        );
    }

    @GetMapping("/filter")
    public List<Product> getProductsByPrice(
            @RequestParam double minPrice,
            @RequestParam double maxPrice
    ) {
        return service.getProductsByPriceRange(
                minPrice,
                maxPrice
        );
    }

    @GetMapping("/advanced")
    public Page<Product> getProductsAdvanced(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String section,
            @RequestParam(required = false) String subCategory,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return service.getProductsAdvanced(
                category,
                section,
                subCategory,
                name,
                minPrice,
                maxPrice,
                page,
                size,
                sort,
                direction
        );
    }
}