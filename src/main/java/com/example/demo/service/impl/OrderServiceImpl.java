package com.example.demo.service.impl;

import com.example.demo.entity.Order;
import com.example.demo.states.OrderStatus;
import com.example.demo.entity.User;
import com.example.demo.states.UserRole;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    /**
     * @param orderRepository
     * @param userRepository
     */
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates new order with set user
     * @param userId
     * @param order
     * @return
     */
    @Override
    public Order createOrder(Long userId, Order order) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        order.setUser(user);
        return orderRepository.save(order);
    }

    /**
     * If user exists fetches all his orders
     * @param userId
     * @return
     */
    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        return orderRepository.findAll()
                .stream()
                .filter(o -> o.getUser().getId().equals(userId))
                .toList();
    }

    /**
     * @param status
     * @return
     */
    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findOrdersByStatus(status);
    }

    /**
     * @param orderId
     * @param newStatus
     * @param updaterUserId
     * @return
     */
    @Override
    public int updateOrderStatus(Long orderId, OrderStatus newStatus, Long updaterUserId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        User updater = userRepository.findById(updaterUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return updateOrderByUserRole(orderId, newStatus, updater, order);
    }

    /**
     * Updating order status
     * If user is of role ADMIN -> can change order status to any
     * If not -> can only cancel order when order is NEW
     * @param orderId
     * @param newStatus
     * @param updater
     * @param order
     * @return
     */
    private int updateOrderByUserRole(Long orderId, OrderStatus newStatus, User updater, Order order) {
        if (updater.getRole() == UserRole.ADMIN) {
            return orderRepository.updateOrderStatusById(orderId, newStatus);
        } else {
            if (newStatus == OrderStatus.CANCELLED) {
                if (order.getStatus() == OrderStatus.NEW) {
                    return orderRepository.updateOrderStatusById(orderId, newStatus);
                } else {
                    throw new IllegalStateException("Cannot cancel an order that is shipped, completed or already cancelled.");
                }
            } else {
                throw new IllegalStateException("Customers can only cancel orders.");
            }
        }
    }
}