package com.example.onlineStore.service;

import com.example.onlineStore.entity.Tag;
import com.example.onlineStore.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public List<Tag> getActiveTags() {
        return tagRepository.findByActiveTrueOrderByNameAsc();
    }

    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    public Optional<Tag> getTagBySlug(String slug) {
        return tagRepository.findBySlug(slug);
    }

    @Transactional
    public Tag createTag(Tag tag) {
        if (tag.getSlug() == null || tag.getSlug().isEmpty()) {
            tag.setSlug(transliterate(tag.getName()));
        }
        return tagRepository.save(tag);
    }

    @Transactional
    public Tag updateTag(Long id, Tag updated) {
        Tag existing = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тег не найден"));

        existing.setName(updated.getName());
        existing.setSlug(updated.getSlug());
        existing.setActive(updated.getActive());

        return tagRepository.save(existing);
    }

    @Transactional
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    private String transliterate(String input) {
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