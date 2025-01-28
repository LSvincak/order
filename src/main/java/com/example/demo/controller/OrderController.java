package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.states.OrderStatus;
import com.example.demo.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Order> createOrder(@PathVariable Long userId, @RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(userId, order));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(OrderStatus.valueOf(status)));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam("newStatus") String newStatus,
            @RequestParam("updaterUserId") Long updaterUserId
    ) {
        int updatedCount;
        try {
            updatedCount = orderService.updateOrderStatus(orderId, OrderStatus.valueOf(newStatus), updaterUserId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        if (updatedCount > 0) {
            return ResponseEntity.ok("Order status updated.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}