package com.example.bookstoreproject.persistance;

import com.example.bookstoreproject.persistance.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {
}
