package com.suranjan.mas.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.suranjan.mas.order.repository.OrderRepository;
import com.suranjan.mas.payment.dto.PaymentResponse;
import com.suranjan.mas.payment.dto.PaymentVerificationRequest;
import com.suranjan.mas.product.entity.Product;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;
    private final OrderRepository orderRepository;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

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
    ) throws Exception {

        JSONObject options = new JSONObject();

        options.put("razorpay_order_id", request.getRazorpayOrderId());
        options.put("razorpay_payment_id", request.getRazorpayPaymentId());
        options.put("razorpay_signature", request.getRazorpaySignature());

        boolean isValid =
                Utils.verifyPaymentSignature(
                        options,
                        razorpaySecret
                );

        if (!isValid) {
            throw new RuntimeException("Invalid payment signature");
        }

        com.suranjan.mas.order.entity.Order order =
                orderRepository.findById(request.getOrderId())
                        .orElseThrow(() ->
                                new RuntimeException("Order not found"));

        if (!order.getPaymentStatus().equalsIgnoreCase("PAID")) {
            order.getItems().forEach(item -> {
                Product product = item.getProduct();

                if (product.getQuantity() < item.getQuantity()) {
                    throw new RuntimeException("Insufficient stock");
                }

                product.setQuantity(
                        product.getQuantity() - item.getQuantity()
                );
            });
        }

        order.setPaymentId(request.getRazorpayPaymentId());
        order.setPaymentMethod("RAZORPAY");
        order.setPaymentStatus("PAID");

        orderRepository.save(order);

        return "Payment verified successfully";
    }
}