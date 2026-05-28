package com.suranjan.mas.order.service;

import com.suranjan.mas.auth.entity.User;
import com.suranjan.mas.cart.entity.Cart;
import com.suranjan.mas.cart.repository.CartRepository;
import com.suranjan.mas.order.dto.OrderItemResponse;
import com.suranjan.mas.order.dto.OrderResponse;
import com.suranjan.mas.order.dto.PlaceOrderRequest;
import com.suranjan.mas.order.dto.PlaceOrderResponse;
import com.suranjan.mas.order.entity.Order;
import com.suranjan.mas.order.entity.OrderItem;
import com.suranjan.mas.order.repository.OrderRepository;
import com.suranjan.mas.product.entity.Product;
import com.suranjan.mas.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(
            CartRepository cartRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public PlaceOrderResponse placeOrder(
            User user,
            PlaceOrderRequest request
    ) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();

        order.setUser(user);
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());

        order.setPaymentMethod(
                request.getPaymentMethod()
        );

        order.setPaymentId(
                request.getPaymentId()
        );

        if (request.getPaymentMethod().equals("ONLINE")) {
            order.setPaymentStatus("PAID");
        } else {
            order.setPaymentStatus("PENDING");
        }

        order.setFullName(request.getFullName());
        order.setPhone(request.getPhone());
        order.setAddress(request.getAddress());
        order.setCity(request.getCity());
        order.setState(request.getState());
        order.setPincode(request.getPincode());

        List<OrderItem> orderItems =
                cart.getItems()
                        .stream()
                        .map(cartItem -> {

                            Product product =
                                    cartItem.getProduct();

                            if (product.getQuantity() <
                                    cartItem.getQuantity()) {

                                throw new RuntimeException(
                                        "Not enough stock for product: "
                                                + product.getName()
                                );
                            }

                            product.setQuantity(
                                    product.getQuantity()
                                            - cartItem.getQuantity()
                            );

                            productRepository.save(product);

                            OrderItem orderItem =
                                    new OrderItem();

                            orderItem.setOrder(order);

                            orderItem.setProduct(product);

                            orderItem.setQuantity(
                                    cartItem.getQuantity()
                            );

                            orderItem.setPrice(
                                    product.getPrice()
                                            * cartItem.getQuantity()
                            );

                            return orderItem;

                        })
                        .toList();

        double totalAmount =
                orderItems.stream()
                        .mapToDouble(
                                OrderItem::getPrice
                        )
                        .sum();

        order.setItems(orderItems);

        order.setTotalAmount(totalAmount);

        Order savedOrder =
                orderRepository.save(order);

        cart.getItems().clear();

        cartRepository.save(cart);

        return new PlaceOrderResponse(
                "Order placed successfully",
                savedOrder.getId()
        );
    }

    public List<OrderResponse> getOrders(User user) {

        List<Order> orders =
                orderRepository.findByUser(user);

        return orders.stream()
                .map(order -> {

                    List<OrderItemResponse> items =
                            order.getItems()
                                    .stream()
                                    .map(item ->
                                            new OrderItemResponse(
                                                    item.getProduct().getName(),
                                                    item.getQuantity(),
                                                    item.getPrice(),
                                                    item.getProduct().getImageUrl()
                                            )
                                    )
                                    .toList();

                    return new OrderResponse(
                            order.getId(),
                            order.getTotalAmount(),
                            order.getStatus(),
                            order.getCreatedAt(),
                            items,
                            order.getPaymentMethod(),
                            order.getPaymentStatus(),
                            order.getPaymentId(),
                            order.getFullName(),
                            order.getPhone(),
                            order.getAddress(),
                            order.getCity(),
                            order.getState(),
                            order.getPincode()
                    );

                })
                .toList();
    }

    public OrderResponse getOrderById(
            User user,
            Long orderId
    ) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found"
                                ));

        if (!order.getUser().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "Unauthorized access"
            );
        }

        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(item ->
                                new OrderItemResponse(
                                        item.getProduct().getName(),
                                        item.getQuantity(),
                                        item.getPrice(),
                                        item.getProduct().getImageUrl()
                                )
                        )
                        .toList();

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                items,
                order.getPaymentMethod(),
                order.getPaymentStatus(),
                order.getPaymentId(),
                order.getFullName(),
                order.getPhone(),
                order.getAddress(),
                order.getCity(),
                order.getState(),
                order.getPincode()
        );
    }

    public String cancelOrder(
            User user,
            Long orderId
    ) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found"
                                ));

        if (!order.getUser().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "Unauthorized access"
            );
        }

        if (order.getStatus()
                .equals("DELIVERED")) {

            throw new RuntimeException(
                    "Delivered order cannot be cancelled"
            );
        }

        order.setStatus("CANCELLED");

        for (OrderItem item : order.getItems()) {

            Product product = item.getProduct();

            product.setQuantity(
                    product.getQuantity()
                            + item.getQuantity()
            );

            productRepository.save(product);
        }

        orderRepository.save(order);

        return "Order cancelled successfully";
    }

    public List<OrderResponse> getAllOrdersForAdmin() {

        List<Order> orders =
                orderRepository.findAll();

        return orders.stream()
                .map(order -> {

                    List<OrderItemResponse> items =
                            order.getItems()
                                    .stream()
                                    .map(item ->
                                            new OrderItemResponse(
                                                    item.getProduct().getName(),
                                                    item.getQuantity(),
                                                    item.getPrice(),
                                                    item.getProduct().getImageUrl()
                                            )
                                    )
                                    .toList();

                    return new OrderResponse(
                            order.getId(),
                            order.getTotalAmount(),
                            order.getStatus(),
                            order.getCreatedAt(),
                            items,
                            order.getPaymentMethod(),
                            order.getPaymentStatus(),
                            order.getPaymentId(),
                            order.getFullName(),
                            order.getPhone(),
                            order.getAddress(),
                            order.getCity(),
                            order.getState(),
                            order.getPincode()
                    );

                })
                .toList();
    }

    public String updateOrderStatus(
            Long orderId,
            String status
    ) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found"
                                ));

        order.setStatus(status);

        orderRepository.save(order);

        return "Order status updated successfully";
    }
}