package com.suranjan.mas.order.service;

import com.suranjan.mas.auth.entity.User;
import com.suranjan.mas.cart.entity.Cart;
import com.suranjan.mas.cart.entity.CartItem;
import com.suranjan.mas.cart.repository.CartRepository;
import com.suranjan.mas.order.dto.OrderItemResponse;
import com.suranjan.mas.order.dto.OrderResponse;
import com.suranjan.mas.order.entity.Order;
import com.suranjan.mas.order.entity.OrderItem;
import com.suranjan.mas.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import com.suranjan.mas.order.dto.PlaceOrderRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public OrderService(CartRepository cartRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    public String placeOrder(User user, PlaceOrderRequest request) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        String paymentMethod = request.getPaymentMethod();

        if (paymentMethod == null) {
            throw new RuntimeException("Payment method is required");
        }

        if (!paymentMethod.equalsIgnoreCase("COD")
                && !paymentMethod.equalsIgnoreCase("RAZORPAY")) {
            throw new RuntimeException("Invalid payment method");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setPaymentMethod(paymentMethod.toUpperCase());
        order.setPaymentStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();

        double totalAmount = 0;

        for (CartItem cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());

            totalAmount += cartItem.getProduct().getPrice()
                    * cartItem.getQuantity();

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);

        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return "Order placed successfully";
    }

    public List<OrderResponse> getOrders(User user) {

        return orderRepository.findByUser(user)
                .stream()
                .map(order -> {

                    List<OrderItemResponse> items =
                            order.getItems()
                                    .stream()
                                    .map(item -> new OrderItemResponse(
                                            item.getProduct().getName(),
                                            item.getQuantity(),
                                            item.getPrice()
                                    ))
                                    .toList();

                    return new OrderResponse(
                            order.getId(),
                            order.getTotalAmount(),
                            order.getStatus(),
                            order.getCreatedAt(),
                            items,
                            order.getPaymentMethod(),
                            order.getPaymentStatus(),
                            order.getPaymentId()
                    );
                })
                .toList();
    }

    public OrderResponse getOrderById(
            User user,
            Long orderId
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(item -> new OrderItemResponse(
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getPrice()
                        ))
                        .toList();

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                items,
                order.getPaymentMethod(),
                order.getPaymentStatus(),
                order.getPaymentId()
        );
    }

    public String updateOrderStatus(
            Long orderId,
            String status
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        order.setStatus(status);

        orderRepository.save(order);

        return "Order status updated";
    }

    public String cancelOrder(User user, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        if (order.getStatus().equalsIgnoreCase("DELIVERED")) {
            throw new RuntimeException(
                    "Delivered order cannot be cancelled"
            );
        }

        order.setStatus("CANCELLED");

        orderRepository.save(order);

        return "Order cancelled successfully";
    }
}