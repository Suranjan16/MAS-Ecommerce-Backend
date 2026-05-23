package com.suranjan.mas.cart.dto;

public class CartItemResponse {

    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;

    public CartItemResponse(
            Long productId,
            String productName,
            Double price,
            Integer quantity
    ) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
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
}