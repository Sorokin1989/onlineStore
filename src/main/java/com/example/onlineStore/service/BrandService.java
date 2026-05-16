package com.example.onlineStore.service;

import com.example.onlineStore.dto.BrandDto;
import com.example.onlineStore.entity.Brand;
import com.example.onlineStore.mapper.BrandMapper;
import com.example.onlineStore.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;  // ← добавить маппер

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public List<Brand> getActiveBrands() {
        return brandRepository.findByActiveTrueOrderByNameAsc();
    }

    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    public Optional<Brand> getBrandBySlug(String slug) {
        return brandRepository.findBySlug(slug);
    }

    public Optional<Brand> getBrandByName(String name) {
        return brandRepository.findByName(name);
    }

    @Transactional
    public BrandDto createBrand(BrandDto brandDto) {  // ← возвращаем DTO
        // Генерируем slug если пустой
        if (brandDto.getSlug() == null || brandDto.getSlug().isEmpty()) {
            brandDto.setSlug(transliterate(brandDto.getName()));
        }

        // DTO → Entity
        Brand brand = brandMapper.toEntity(brandDto);

        // Сохраняем Entity
        Brand savedBrand = brandRepository.save(brand);

        // Entity → DTO и возвращаем
        return brandMapper.toDto(savedBrand);
    }

    @Transactional
    public BrandDto updateBrand(Long id, BrandDto brandDto) {  // ← возвращаем DTO
        Brand existing = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бренд не найден"));

        // Обновляем поля
        existing.setName(brandDto.getName());
        existing.setSlug(brandDto.getSlug());
        existing.setLogo(brandDto.getLogo());
        existing.setDescription(brandDto.getDescription());
        existing.setSortOrder(brandDto.getSortOrder() != null ? brandDto.getSortOrder() : 0);
        existing.setActive(brandDto.getActive());

        // Сохраняем
        Brand updatedBrand = brandRepository.save(existing);

        // Entity → DTO и возвращаем
        return brandMapper.toDto(updatedBrand);
    }

    @Transactional
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }

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