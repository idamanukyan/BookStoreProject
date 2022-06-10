package com.example.bookstoreproject.persistance.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "isbn")
    private int isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "price")
    private double price;

    @Column(name = "image_url_s")
    private String imageUrlS;

    @Column(name = "image_url_m")
    private String imageUrlM;

    @Column(name = "image_url_l")
    private String imageUrlL;

    @Column(name = "author_name")
    private String authorName;

    @ManyToOne
    private PublisherEntity publisher;

    @ManyToOne
    private CategoryEntity category;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    private List<RatingEntity> bookRatings;


    @ManyToMany(mappedBy = "books")
    private List<AuthorEntity> authors;

}
