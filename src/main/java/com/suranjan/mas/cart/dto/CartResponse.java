package com.suranjan.mas.cart.dto;

import java.util.List;

public class CartResponse {

    private List<CartItemResponse> items;
    private Double totalAmount;

    public CartResponse(
            List<CartItemResponse> items,
            Double totalAmount
    ) {
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
}