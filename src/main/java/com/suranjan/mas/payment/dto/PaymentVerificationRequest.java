package com.suranjan.mas.payment.dto;

public class PaymentVerificationRequest {

    private Long orderId;

    private String razorpayPaymentId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(
            String razorpayPaymentId
    ) {
        this.razorpayPaymentId =
                razorpayPaymentId;
    }
}