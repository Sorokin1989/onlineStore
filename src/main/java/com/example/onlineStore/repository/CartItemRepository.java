package com.example.onlineStore.repository;

import com.example.onlineStore.entity.CartItem;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findByUser(User user);

    List<CartItem> findBySessionId(String sessionId);

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    Optional<CartItem> findBySessionIdAndProduct(String sessionId, Product product);

    void deleteByUser(User user);

    void deleteBySessionId(String sessionId);
}
