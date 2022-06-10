package com.example.bookstoreproject.persistance.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "age")
    private int age;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
     private String password;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType usertype;

    @Column(name = "has_membership_card")
    private boolean hasMembershipCard;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<RatingEntity> bookRatings;

    @ManyToOne
    private CityEntity location;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<CustomerEntity> customers;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<AuthorEntity> authors;
}
