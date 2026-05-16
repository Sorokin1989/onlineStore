package com.example.onlineStore.controller;

import com.example.onlineStore.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/images")
@RequiredArgsConstructor
public class AdminImageController {

    private final FileStorageService fileStorageService;

    @GetMapping
    public String listImages(Model model) {
        List<String> images = new ArrayList<>();
        Path uploadPath = Paths.get(fileStorageService.getUploadPath());
        try {
            if (Files.exists(uploadPath)) {
                Files.walk(uploadPath, 1)
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            String fileName = path.getFileName().toString();
                            if (fileName.matches(".*\\.(jpg|jpeg|png|gif|webp)$")) {
                                images.add(fileName);
                            }
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("images", images);
        model.addAttribute("content", "pages/admin/images/list :: content");
        return "layouts/main";
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file,
                              @RequestParam(value = "subDir", required = false) String subDir,
                              RedirectAttributes redirectAttributes) {
        try {
            String uploadDir = subDir != null ? subDir : "general";
            String filePath = fileStorageService.saveFile(file, uploadDir);
            redirectAttributes.addFlashAttribute("success", "Файл загружен: " + filePath);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при загрузке файла");
        }
        return "redirect:/admin/images";
    }

    @PostMapping("/delete/{filename}")
    public String deleteImage(@PathVariable String filename,
                              @RequestParam(value = "subDir", required = false) String subDir,
                              RedirectAttributes redirectAttributes) {
        try {
            String uploadDir = subDir != null ? subDir : "general";
            Path filePath = Paths.get(fileStorageService.getUploadPath(), uploadDir, filename);
            Files.deleteIfExists(filePath);
            redirectAttributes.addFlashAttribute("success", "Файл удалён: " + filename);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении файла");
        }
        return "redirect:/admin/images";
    }
}