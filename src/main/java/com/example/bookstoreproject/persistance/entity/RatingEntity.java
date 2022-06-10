package com.example.bookstoreproject.persistance.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rating")
public class RatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "rating")
    private int rating;

    @Column(name = "feedback")
    private String feedback;

    @ManyToOne
    private BookEntity book;

    @ManyToOne
    private UserEntity user;
}
