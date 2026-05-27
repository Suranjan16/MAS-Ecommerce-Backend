package com.suranjan.mas.product.dto;

public class ProductResponse {

    private Long id;

    private String name;

    private String category;

    private String section;

    private String subCategory;

    private Double price;

    private Integer quantity;

    private String imageUrl;

    public ProductResponse(
            Long id,
            String name,
            String category,
            String section,
            String subCategory,
            Double price,
            Integer quantity,
            String imageUrl
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.section = section;
        this.subCategory = subCategory;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getSection() {
        return section;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}