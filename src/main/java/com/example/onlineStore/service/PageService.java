package com.example.onlineStore.service;

import com.example.onlineStore.entity.Page;
import com.example.onlineStore.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;

    // Получить все страницы
    public List<Page> getAllPages() {
        return pageRepository.findAll();
    }

    // Получить активные страницы (для меню)
    public List<Page> getActivePages() {
        return pageRepository.findByActiveTrueOrderBySortOrderAsc();
    }

    // Получить страницу по Slug
    public Optional<Page> getPageBySlug(String slug) {
        return pageRepository.findBySlug(slug);
    }

    // Получить страницу по ID
    public Optional<Page> getPageById(Long id) {
        return pageRepository.findById(id);
    }

    // Создать новую страницу
    @Transactional
    public Page createPage(Page page) {
        if (page.getSlug() == null || page.getSlug().isEmpty()) {
            page.setSlug(transliterate(page.getTitle()));
        }
        return pageRepository.save(page);
    }

    // Обновить страницу
    @Transactional
    public Page updatePage(Long id, Page updatedPage) {
        Page existing = pageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Страница не найдена"));

        existing.setTitle(updatedPage.getTitle());
        existing.setSlug(updatedPage.getSlug());
        existing.setContent(updatedPage.getContent());
        existing.setMetaTitle(updatedPage.getMetaTitle());
        existing.setMetaDescription(updatedPage.getMetaDescription());
        existing.setSortOrder(updatedPage.getSortOrder());
        existing.setActive(updatedPage.getActive());

        return pageRepository.save(existing);
    }

    // Удалить страницу
    @Transactional
    public void deletePage(Long id) {
        pageRepository.deleteById(id);
    }

    // Транслитерация для генерации slug
    private String transliterate(String input) {
        if (input == null) return "";
        return input.toLowerCase()
                .replace(" ", "-")
                .replace("а", "a").replace("б", "b").replace("в", "v")
                .replace("г", "g").replace("д", "d").replace("е", "e")
                .replace("ё", "e").replace("ж", "zh").replace("з", "z")
                .replace("и", "i").replace("й", "y").replace("к", "k")
                .replace("л", "l").replace("м", "m").replace("н", "n")
                .replace("о", "o").replace("п", "p").replace("р", "r")
                .replace("с", "s").replace("т", "t").replace("у", "u")
                .replace("ф", "f").replace("х", "kh").replace("ц", "ts")
                .replace("ч", "ch").replace("ш", "sh").replace("щ", "sch")
                .replace("ъ", "").replace("ы", "y").replace("ь", "")
                .replace("э", "e").replace("ю", "yu").replace("я", "ya")
                .replaceAll("[^a-z0-9-]", "");
    }
}