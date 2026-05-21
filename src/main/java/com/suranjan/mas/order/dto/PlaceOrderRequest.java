package com.suranjan.mas.order.dto;

public class PlaceOrderRequest {

    private String paymentMethod; // COD or RAZORPAY

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}