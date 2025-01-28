package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.states.OrderStatus;
import java.util.List;

public interface ExtendedOrderRepository {
    List<Order> findOrdersByStatus(OrderStatus status);
    int updateOrderStatusById(Long orderId, OrderStatus newStatus);
}