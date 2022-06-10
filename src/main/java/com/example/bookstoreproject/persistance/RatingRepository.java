package com.example.bookstoreproject.persistance;

import com.example.bookstoreproject.persistance.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {
}
