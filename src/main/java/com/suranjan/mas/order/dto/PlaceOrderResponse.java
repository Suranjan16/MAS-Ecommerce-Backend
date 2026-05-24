package com.suranjan.mas.order.dto;

public class PlaceOrderResponse {

    private String message;
    private Long orderId;

    public PlaceOrderResponse(String message, Long orderId) {
        this.message = message;
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public Long getOrderId() {
        return orderId;
    }
}