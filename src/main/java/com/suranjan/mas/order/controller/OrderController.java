package com.suranjan.mas.order.controller;

import com.suranjan.mas.auth.entity.User;
import com.suranjan.mas.auth.repository.UserRepository;
import com.suranjan.mas.order.entity.Order;
import com.suranjan.mas.order.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @PostMapping("/place")
    public String placeOrder(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderService.placeOrder(user);
    }

    @GetMapping
    public List<Order> getOrders(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderService.getOrders(user);
    }
}
