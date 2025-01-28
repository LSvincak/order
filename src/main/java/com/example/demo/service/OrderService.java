package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.states.OrderStatus;

import java.util.List;

public interface OrderService {
    Order createOrder(Long userId, Order order);
    List<Order> getOrdersByUserId(Long userId);
    List<Order> getOrdersByStatus(OrderStatus status);
    int updateOrderStatus(Long orderId, OrderStatus newStatus, Long updaterUserId);
}