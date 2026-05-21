package com.suranjan.mas.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;

    public PaymentService(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    public String createPaymentOrder(Double amount)
            throws Exception {

        JSONObject options = new JSONObject();

        options.put("amount", amount * 100);

        options.put("currency", "INR");

        options.put("receipt",
                "txn_" + System.currentTimeMillis());

        Order order =
                razorpayClient.orders.create(options);

        return order.toString();
    }
}