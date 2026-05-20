package com.suranjan.mas.order.dto;

public class OrderItemResponse {
    private String productName;
    private Integer quantity;
    private Double price;

    public OrderItemResponse(String productName, Integer quantity, Double price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
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
}
