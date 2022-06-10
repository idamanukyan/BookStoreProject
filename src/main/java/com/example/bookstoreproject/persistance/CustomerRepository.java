package com.example.bookstoreproject.persistance;

import com.example.bookstoreproject.persistance.entity.CustomerEntity;
import com.example.bookstoreproject.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

    public Optional<UserEntity> findCustomerEntitiesByUser_Email(String email);
}
