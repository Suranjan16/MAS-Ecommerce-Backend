package com.suranjan.mas.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.suranjan.mas.order.repository.OrderRepository;
import com.suranjan.mas.payment.dto.PaymentResponse;
import com.suranjan.mas.payment.dto.PaymentVerificationRequest;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;
    private final OrderRepository orderRepository;

    public PaymentService(
            RazorpayClient razorpayClient,
            OrderRepository orderRepository
    ) {
        this.razorpayClient = razorpayClient;
        this.orderRepository = orderRepository;
    }

    public PaymentResponse createPaymentOrder(Long orderId) throws Exception {

        com.suranjan.mas.order.entity.Order orderEntity =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException("Order not found"));

        JSONObject options = new JSONObject();

        options.put("amount", orderEntity.getTotalAmount() * 100);
        options.put("currency", "INR");
        options.put("receipt", "order_" + orderId);

        Order razorpayOrder =
                razorpayClient.orders.create(options);

        return new PaymentResponse(
                razorpayOrder.get("id"),
                razorpayOrder.get("amount"),
                razorpayOrder.get("currency")
        );
    }

    public String verifyPayment(
            PaymentVerificationRequest request
    ) {

        com.suranjan.mas.order.entity.Order order =
                orderRepository.findById(
                                request.getOrderId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found"
                                ));

        order.setPaymentId(
                request.getRazorpayPaymentId()
        );

        order.setPaymentStatus("PAID");

        order.setStatus("PAID");

        orderRepository.save(order);

        return "Payment verified successfully";
    }
}