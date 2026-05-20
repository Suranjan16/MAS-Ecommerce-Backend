package com.suranjan.mas.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long orderId;

    private Double totalAmount;

    private String status;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;

    public OrderResponse(
            Long orderId,
            Double totalAmount,
            String status,
            LocalDateTime createdAt,
            List<OrderItemResponse> items
    ) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }
}
