package com.example.bookstoreproject.persistance;

import com.example.bookstoreproject.persistance.entity.MembershipCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipCardRepository extends JpaRepository<MembershipCardEntity, Integer> {
}
