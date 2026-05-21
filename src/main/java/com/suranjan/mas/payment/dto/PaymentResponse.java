package com.suranjan.mas.payment.dto;

public class PaymentResponse {

    private String orderId;

    private Integer amount;

    private String currency;

    public PaymentResponse(
            String orderId,
            Integer amount,
            String currency
    ) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
    }

    public String getOrderId() {
        return orderId;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}