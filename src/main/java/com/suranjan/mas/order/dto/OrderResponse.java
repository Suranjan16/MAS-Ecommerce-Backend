package com.suranjan.mas.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long orderId;

    private Double totalAmount;

    private String status;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;

    private String paymentMethod;
    private String paymentStatus;
    private String paymentId;


    public OrderResponse(
            Long orderId,
            Double totalAmount,
            String status,
            LocalDateTime createdAt,
            List<OrderItemResponse> items,
            String paymentMethod,
            String paymentStatus,
            String paymentId
    ) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
        this.paymentId = paymentId;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getPaymentId() {
        return paymentId;
    }
}
