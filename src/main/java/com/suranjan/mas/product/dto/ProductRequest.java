package com.suranjan.mas.product.dto;

public class ProductRequest {

    private String name;

    private String category;

    private String section;

    private String subCategory;

    private Double price;

    private Integer quantity;

    private String imageUrl;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}