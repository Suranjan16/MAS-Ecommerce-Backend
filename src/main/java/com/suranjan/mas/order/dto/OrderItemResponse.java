package com.suranjan.mas.order.dto;

public class OrderItemResponse {

    private String productName;
    private Integer quantity;
    private Double price;
    private String imageUrl;

    public OrderItemResponse(
            String productName,
            Integer quantity,
            Double price,
            String imageUrl
    ) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
