package com.example.bookstoreproject.persistance.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "credit_card")
public class CreditCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "cardholder_name")
    private String cardholderName;

    @Column(name = "cvv")
    private int cvv;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_card_id")
    private List<MembershipCardEntity> membershipCards;
}
