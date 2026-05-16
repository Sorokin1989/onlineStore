package com.example.onlineStore.controller;

import com.example.onlineStore.dto.BrandDto;
import com.example.onlineStore.entity.Brand;
import com.example.onlineStore.mapper.BrandMapper;
import com.example.onlineStore.service.BrandService;
import com.example.onlineStore.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/brands")
@RequiredArgsConstructor
public class AdminBrandController {

    private final BrandService brandService;
    private final BrandMapper brandMapper;
    private final FileStorageService fileStorageService;

    @GetMapping
    public String listBrands(Model model) {
        model.addAttribute("brands", brandMapper.toDtoList(brandService.getAllBrands()));
        model.addAttribute("content", "pages/admin/brands/list :: content");
        return "layouts/main";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("brand", new BrandDto());
        model.addAttribute("content", "pages/admin/brands/form :: content");
        return "layouts/main";
    }

    @PostMapping("/create")
    public String createBrand(@ModelAttribute BrandDto brandDto,
                              RedirectAttributes redirectAttributes) {
        try {
            // Получаем файл из DTO
            MultipartFile logoFile = brandDto.getLogoFile();

            if (logoFile != null && !logoFile.isEmpty()) {
                String logoPath = fileStorageService.saveFile(logoFile, "brands");
                brandDto.setLogo(logoPath);
            }

            brandService.createBrand(brandDto);
            redirectAttributes.addFlashAttribute("success", "Бренд создан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/admin/brands";
    }

    @PostMapping("/update/{id}")
    public String updateBrand(@PathVariable Long id,
                              @ModelAttribute BrandDto brandDto,
                              RedirectAttributes redirectAttributes) {
        try {
            MultipartFile logoFile = brandDto.getLogoFile();

            if (logoFile != null && !logoFile.isEmpty()) {
                String logoPath = fileStorageService.saveFile(logoFile, "brands");
                brandDto.setLogo(logoPath);
            }

            brandService.updateBrand(id, brandDto);
            redirectAttributes.addFlashAttribute("success", "Бренд обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/admin/brands";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Brand brand = brandService.getBrandById(id)
                .orElseThrow(() -> new RuntimeException("Бренд не найден"));
        model.addAttribute("brand", brandMapper.toDto(brand));
        model.addAttribute("content", "pages/admin/brands/form :: content");
        return "layouts/main";
    }

    @PostMapping("/delete/{id}")
    public String deleteBrand(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            brandService.deleteBrand(id);
            redirectAttributes.addFlashAttribute("success", "Бренд удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении бренда");
        }
        return "redirect:/admin/brands";
    }
}