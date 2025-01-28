package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.states.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ExtendedOrderRepositoryImpl implements ExtendedOrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Order> findOrdersByStatus(OrderStatus status) {
        TypedQuery<Order> query = entityManager.createNamedQuery("Order.findByStatus", Order.class);
        query.setParameter("status", status.name());
        return query.getResultList();
    }

    @Override
    public int updateOrderStatusById(Long orderId, OrderStatus newStatus) {
        Query query = entityManager.createQuery(
                "UPDATE Order o SET o.status = :newStatus WHERE o.id = :orderId"
        );
        query.setParameter("newStatus", newStatus);
        query.setParameter("orderId", orderId);
        return query.executeUpdate();
    }
}