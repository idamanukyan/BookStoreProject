package com.example.bookstoreproject.persistance;

import com.example.bookstoreproject.persistance.entity.CreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCardEntity, Integer> {

     Optional<CreditCardEntity> findByCardNumber(String cardNumber);
}
