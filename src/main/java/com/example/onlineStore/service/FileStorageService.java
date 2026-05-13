package com.example.onlineStore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.path:/tmp/uploads/}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        try {
            Path path = Paths.get(uploadPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать папку для загрузок", e);
        }
    }

    // Сохранить файл
    public String saveFile(MultipartFile file, String subDir) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Создаём поддиректорию
        Path dirPath = Paths.get(uploadPath, subDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // Генерируем уникальное имя файла
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFileName = UUID.randomUUID().toString() + extension;

        // Сохраняем файл
        Path filePath = dirPath.resolve(newFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Возвращаем относительный путь
        return "/uploads/" + subDir + "/" + newFileName;
    }

    // Удалить файл
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) return;

        try {
            // Извлекаем файловый путь из URL
            String relativePath = filePath.replace("/uploads/", "");
            Path fullPath = Paths.get(uploadPath, relativePath);
            Files.deleteIfExists(fullPath);
        } catch (IOException e) {
            // Логируем, но не бросаем исключение
            System.err.println("Не удалось удалить файл: " + filePath);
        }
    }

    // Получить полный путь для сохранения
    public String getSubDirPath(String subDir) {
        return Paths.get(uploadPath, subDir).toString();
    }
}