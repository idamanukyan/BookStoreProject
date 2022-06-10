package com.example.bookstoreproject.persistance.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "author")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "total_book_count")
    private int totalBookCount;

    @ManyToOne
    private UserEntity user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_author", joinColumns = {@JoinColumn(referencedColumnName = "id")}
            , inverseJoinColumns = {@JoinColumn(referencedColumnName = "id")})
    private List<BookEntity> books;

}
