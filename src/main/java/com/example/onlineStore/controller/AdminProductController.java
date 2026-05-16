package com.example.onlineStore.controller;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Brand;
import com.example.onlineStore.entity.Category;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.Warranty;
import com.example.onlineStore.mapper.ProductMapper;
import com.example.onlineStore.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductMapper productMapper;
    private final FileStorageService fileStorageService;
    private final WarrantyService warrantyService;

    // Список товаров
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", productMapper.toDtoList(products));
        model.addAttribute("content", "pages/admin/products/list");
        return "layouts/main";
    }

    // Форма создания товара
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("content", "pages/admin/products/form");
        return "layouts/main";
    }

    // Сохранение товара
    @PostMapping("/create")
    public String createProduct(@ModelAttribute ProductDto productDto,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        try {
            // Проверка обязательных полей
            if (productDto.getCategoryId() == null) {
                model.addAttribute("error", "Категория не выбрана");
                model.addAttribute("product", productDto);  // ← возвращаем заполненные данные
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("brands", brandService.getAllBrands());
                model.addAttribute("content", "pages/admin/products/form");
                return "layouts/main";
            }

            if (productDto.getBrandId() == null) {
                model.addAttribute("error", "Бренд не выбран");
                model.addAttribute("product", productDto);
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("brands", brandService.getAllBrands());
                model.addAttribute("content", "pages/admin/products/form");
                return "layouts/main";
            }

            if (productDto.getName() == null || productDto.getName().trim().isEmpty()) {
                model.addAttribute("error", "Название товара обязательно");
                model.addAttribute("product", productDto);
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("brands", brandService.getAllBrands());
                model.addAttribute("content", "pages/admin/products/form");
                return "layouts/main";
            }

            MultipartFile mainImage = productDto.getMainImageFile();

            if (mainImage != null && !mainImage.isEmpty()) {
                String imagePath = fileStorageService.saveFile(mainImage, "products");
                productDto.setImagePath(imagePath);
            }

            productDto.setId(null);

            Category category = categoryService.getCategoryById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Категория не найдена"));
            Brand brand = brandService.getBrandById(productDto.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Бренд не найден"));

            Product product = productMapper.toEntity(productDto, category, brand);
            productService.createProduct(product);

            redirectAttributes.addFlashAttribute("success", "Товар успешно создан");
            return "redirect:/admin/products";

        } catch (Exception e) {
            // При любой ошибке возвращаем форму с заполненными данными
            model.addAttribute("error", "Ошибка при создании товара: " + e.getMessage());
            model.addAttribute("product", productDto);  // ← сохраняем введённые данные
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("content", "pages/admin/products/form");
            return "layouts/main";
        }
    }
    // Обновление товара
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute ProductDto productDto,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        try {
            if (productDto.getCategoryId() == null) {
                model.addAttribute("error", "Категория не выбрана");
                model.addAttribute("product", productDto);
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("brands", brandService.getAllBrands());
                model.addAttribute("content", "pages/admin/products/form");
                return "layouts/main";
            }

            if (productDto.getBrandId() == null) {
                model.addAttribute("error", "Бренд не выбран");
                model.addAttribute("product", productDto);
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("brands", brandService.getAllBrands());
                model.addAttribute("content", "pages/admin/products/form");
                return "layouts/main";
            }

            MultipartFile mainImage = productDto.getMainImageFile();

            if (mainImage != null && !mainImage.isEmpty()) {
                String imagePath = fileStorageService.saveFile(mainImage, "products");
                productDto.setImagePath(imagePath);
            }

            Category category = categoryService.getCategoryById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Категория не найдена"));
            Brand brand = brandService.getBrandById(productDto.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Бренд не найден"));

            Product product = productMapper.toEntity(productDto, category, brand);
            productService.updateProduct(id, product);

            redirectAttributes.addFlashAttribute("success", "Товар успешно обновлён");
            return "redirect:/admin/products";

        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при обновлении товара: " + e.getMessage());
            model.addAttribute("product", productDto);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("content", "pages/admin/products/form");
            return "layouts/main";
        }
    }
    // Форма редактирования товара
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        model.addAttribute("product", productMapper.toDto(product));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("content", "pages/admin/products/form");
        return "layouts/main";
    }

    // Удаление товара
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Товар удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении товара");
        }
        return "redirect:/admin/products";
    }

    // Управление изображениями товара
    @GetMapping("/images/{productId}")
    public String manageImages(@PathVariable Long productId, Model model) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("content", "pages/admin/products/images");
        return "layouts/main";
    }

    @PostMapping("/images/{productId}/upload")
    public String uploadImage(@PathVariable Long productId,
                              @RequestParam MultipartFile image,
                              RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Товар не найден"));

            String imagePath = fileStorageService.saveFile(image, "products");
            productService.addImage(product, imagePath);

            redirectAttributes.addFlashAttribute("success", "Изображение добавлено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при загрузке изображения");
        }
        return "redirect:/admin/products/images/" + productId;
    }

    @PostMapping("/images/{imageId}/delete")
    public String deleteImage(@PathVariable Long imageId,
                              RedirectAttributes redirectAttributes) {
        try {
            Long productId = productService.getProductIdByImageId(imageId);
            productService.deleteImage(imageId);
            redirectAttributes.addFlashAttribute("success", "Изображение удалено");
            return "redirect:/admin/products/images/" + productId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении изображения");
            return "redirect:/admin/products";
        }
    }
}