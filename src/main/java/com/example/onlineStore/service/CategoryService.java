package com.example.onlineStore.service;

import com.example.onlineStore.entity.Category;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Получить все категории
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Получить корневые категории (без родителей)
    public List<Category> getRootCategories() {
        return categoryRepository.findByParentIsNull();
    }

    // Получить категорию по Slug
    public Optional<Category> getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    // Получить категорию по ID
    public Optional<Category> getCategoryById(Long id) {
        if (id == null) {
            return Optional.empty();  // ← защита от null
        }
        return categoryRepository.findById(id);
    }

    // Создать новую категорию
    @Transactional
    public Category createCategory(Category category) {
        if (category.getSlug() == null || category.getSlug().isEmpty()) {
            category.setSlug(transliterate(category.getName()));
        }
        return categoryRepository.save(category);
    }

    // Обновить категорию
    @Transactional
    public Category updateCategory(Long id, Category updated) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        existing.setName(updated.getName());
        existing.setSlug(updated.getSlug());
        existing.setDescription(updated.getDescription());
        existing.setImagePath(updated.getImagePath());
        existing.setSortOrder(updated.getSortOrder());
        existing.setActive(updated.getActive());

        if (updated.getParent() != null) {
            existing.setParent(updated.getParent());
        }

        return categoryRepository.save(existing);
    }

    // Удалить категорию
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        // Перемещаем товары в родительскую категорию или делаем без категории
        if (category.getParent() != null) {
            for (Product product : category.getProducts()) {
                product.setCategory(category.getParent());
            }
        }

        categoryRepository.delete(category);
    }

    // Простая транслитерация для генерации slug
    private String transliterate(String input) {
        return input.toLowerCase()
                .replace("а", "a").replace("б", "b").replace("в", "v").replace("г", "g")
                .replace("д", "d").replace("е", "e").replace("ё", "e").replace("ж", "zh")
                .replace("з", "z").replace("и", "i").replace("й", "y").replace("к", "k")
                .replace("л", "l").replace("м", "m").replace("н", "n").replace("о", "o")
                .replace("п", "p").replace("р", "r").replace("с", "s").replace("т", "t")
                .replace("у", "u").replace("ф", "f").replace("х", "kh").replace("ц", "ts")
                .replace("ч", "ch").replace("ш", "sh").replace("щ", "sch").replace("ъ", "")
                .replace("ы", "y").replace("ь", "").replace("э", "e").replace("ю", "yu")
                .replace("я", "ya").replace(" ", "-")
                .replaceAll("[^a-z0-9-]", "");
    }
}