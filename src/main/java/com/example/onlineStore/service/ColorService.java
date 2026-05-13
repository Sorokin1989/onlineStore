package com.example.onlineStore.service;

import com.example.onlineStore.entity.Color;
import com.example.onlineStore.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ColorService {

    private final ColorRepository colorRepository;

    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    public List<Color> getActiveColors() {
        return colorRepository.findByActiveTrueOrderByNameAsc();
    }

    public Optional<Color> getColorById(Long id) {
        return colorRepository.findById(id);
    }

    public Optional<Color> getColorByName(String name) {
        return colorRepository.findByName(name);
    }

    @Transactional
    public Color createColor(Color color) {
        return colorRepository.save(color);
    }

    @Transactional
    public Color updateColor(Long id, Color updated) {
        Color existing = colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Цвет не найден"));

        existing.setName(updated.getName());
        existing.setHex(updated.getHex());
        existing.setActive(updated.getActive());

        return colorRepository.save(existing);
    }

    @Transactional
    public void deleteColor(Long id) {
        colorRepository.deleteById(id);
    }
}