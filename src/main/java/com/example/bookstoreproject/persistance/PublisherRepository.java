package com.example.bookstoreproject.persistance;

import com.example.bookstoreproject.persistance.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<PublisherEntity, Integer> {

    public Optional<PublisherEntity> findByPhoneNumber(String phoneNumber);
}
