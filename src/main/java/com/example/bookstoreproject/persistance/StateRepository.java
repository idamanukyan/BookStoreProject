package com.example.bookstoreproject.persistance;

import com.example.bookstoreproject.persistance.entity.CityEntity;
import com.example.bookstoreproject.persistance.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StateRepository extends JpaRepository<StateEntity, Integer> {

    public Optional<StateEntity> findByNameAndCountry_Name(String name, String countryName);
}
