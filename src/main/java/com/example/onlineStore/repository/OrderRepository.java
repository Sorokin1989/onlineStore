package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Order;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findAllByOrderByCreatedAtDesc();

    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);

    // OrderRepository.java
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o " +
            "JOIN o.items i " +
            "WHERE o.user = :user " +
            "AND o.status IN :statuses " +
            "AND i.product.id = :productId")
    boolean existsByUserAndProductAndStatusIn(@Param("user") User user,
                                              @Param("productId") Long productId,
                                              @Param("statuses") List<OrderStatus> statuses);
}
