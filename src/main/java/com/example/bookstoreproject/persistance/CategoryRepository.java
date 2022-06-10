package com.example.bookstoreproject.persistance;

import com.example.bookstoreproject.persistance.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    Optional<CategoryEntity> findByCategory(String category);
}
