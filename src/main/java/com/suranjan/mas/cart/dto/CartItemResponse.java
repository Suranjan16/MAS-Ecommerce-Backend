package com.suranjan.mas.cart.dto;

public class CartItemResponse {

    private Long productId;

    private String productName;

    private Double price;

    private Integer quantity;

    private String imageUrl;

    public CartItemResponse(
            Long productId,
            String productName,
            Double price,
            Integer quantity,
            String imageUrl
    ) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
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