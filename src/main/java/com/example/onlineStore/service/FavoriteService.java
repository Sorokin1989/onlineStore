package com.example.onlineStore.service;

import com.example.onlineStore.entity.Favorite;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    // Получить все избранные товары пользователя
    public List<Favorite> getFavoritesByUser(User user) {
        return favoriteRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // Получить ID всех избранных товаров пользователя
    public List<Long> getFavoriteProductIdsByUser(User user) {
        return favoriteRepository.findProductIdsByUser(user);
    }

    // Проверить, находится ли товар в избранном у пользователя
    public boolean isProductInFavorites(User user, Product product) {
        return favoriteRepository.existsByUserAndProduct(user, product);
    }

    // Получить избранное по пользователю и товару
    public Optional<Favorite> getFavoriteByUserAndProduct(User user, Product product) {
        return favoriteRepository.findByUserAndProduct(user, product);
    }

    // Добавить товар в избранное
    @Transactional
    public Favorite addToFavorites(User user, Product product) {
        // Проверяем, не добавлен ли уже
        if (isProductInFavorites(user, product)) {
            throw new RuntimeException("Товар уже в избранном");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favorite.setCreatedAt(java.time.LocalDateTime.now());

        return favoriteRepository.save(favorite);
    }

    // Удалить товар из избранного
    @Transactional
    public void removeFromFavorites(User user, Long productId) {
        favoriteRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    // Удалить все избранные товары пользователя
    @Transactional
    public void clearFavorites(User user) {
        favoriteRepository.deleteByUser(user);
    }

    // Получить количество избранных товаров пользователя
    public long getFavoritesCount(User user) {
        return favoriteRepository.countByUser(user);
    }
}