package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpecificationRepository extends JpaRepository<Specification, Long> {

    Optional<Specification> findByName(String name);

    List<Specification> findByActiveTrueOrderByNameAsc();

    List<Specification> findByFilterableTrue();
}