package com.example.bookstoreproject.persistance;

import com.example.bookstoreproject.persistance.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<CountryEntity, Integer> {

    public Optional<CountryEntity> findByName(String name);
}
