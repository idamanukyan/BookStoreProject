package com.example.bookstoreproject.persistance;

import com.example.bookstoreproject.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

     Optional<UserEntity> findUserEntityByEmail(String email);

     Optional<UserEntity> findByEmail(String email);

}
