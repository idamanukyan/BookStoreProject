package com.example.bookstoreproject.persistance.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "membership_card")
public class MembershipCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @ManyToOne
    private CreditCardEntity creditCard;

}
