package com.suranjan.mas.order.repository;

import com.suranjan.mas.auth.entity.User;
import com.suranjan.mas.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
