package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CategoryDto;
import com.example.onlineStore.entity.Category;
import com.example.onlineStore.mapper.CategoryMapper;
import com.example.onlineStore.service.CategoryService;
import com.example.onlineStore.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final FileStorageService fileStorageService;

    // Список категорий
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryMapper.toDtoList(categoryService.getAllCategories()));
        model.addAttribute("content", "pages/admin/categories/list");
        return "layouts/main";
    }

    // Форма создания категории
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("category", new CategoryDto());
        model.addAttribute("parentCategories", categoryService.getRootCategories());
        model.addAttribute("content", "pages/admin/categories/form");
        return "layouts/main";
    }

    // Сохранение категории
    @PostMapping("/create")
    public String createCategory(@ModelAttribute CategoryDto categoryDto,
                                 @RequestParam(required = false) MultipartFile image,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        try {
            // Валидация
            if (categoryDto.getName() == null || categoryDto.getName().trim().isEmpty()) {
                model.addAttribute("error", "Название категории обязательно");
                model.addAttribute("category", categoryDto);
                model.addAttribute("parentCategories", categoryService.getRootCategories());
                model.addAttribute("content", "pages/admin/categories/form");
                return "layouts/main";
            }

            if (image != null && !image.isEmpty()) {
                String imagePath = fileStorageService.saveFile(image, "categories");
                categoryDto.setImagePath(imagePath);
            }

            Category parent = categoryDto.getParentId() != null
                    ? categoryService.getCategoryById(categoryDto.getParentId()).orElse(null)
                    : null;

            Category category = categoryMapper.toEntity(categoryDto, parent);
            categoryService.createCategory(category);

            redirectAttributes.addFlashAttribute("success", "Категория создана");
            return "redirect:/admin/categories";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании категории: " + e.getMessage());
            return "redirect:/admin/categories/create";
        }
    }

    // Форма редактирования категории
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        model.addAttribute("category", categoryMapper.toDto(category));
        model.addAttribute("parentCategories", categoryService.getRootCategories());
        model.addAttribute("content", "pages/admin/categories/form");
        return "layouts/main";
    }

    // Обновление категории
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute CategoryDto categoryDto,
                                 @RequestParam(required = false) MultipartFile image,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        try {
            // Валидация
            if (categoryDto.getName() == null || categoryDto.getName().trim().isEmpty()) {
                model.addAttribute("error", "Название категории обязательно");
                model.addAttribute("category", categoryDto);
                model.addAttribute("parentCategories", categoryService.getRootCategories());
                model.addAttribute("content", "pages/admin/categories/form");
                return "layouts/main";
            }

            if (image != null && !image.isEmpty()) {
                String imagePath = fileStorageService.saveFile(image, "categories");
                categoryDto.setImagePath(imagePath);
            }

            Category parent = categoryDto.getParentId() != null
                    ? categoryService.getCategoryById(categoryDto.getParentId()).orElse(null)
                    : null;

            Category category = categoryMapper.toEntity(categoryDto, parent);
            categoryService.updateCategory(id, category);

            redirectAttributes.addFlashAttribute("success", "Категория обновлена");
            return "redirect:/admin/categories";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении категории: " + e.getMessage());
            return "redirect:/admin/categories/edit/" + id;
        }
    }

    // Удаление категории
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Категория удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении категории: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }
}