package com.example.bookstoreproject.service.dto;

import com.example.bookstoreproject.service.dto.CategoryDto;
import com.example.bookstoreproject.service.dto.PublisherDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private int id;
    private int isbn;
    private String title;
    private LocalDate publicationDate;
    private String imageUrlS;
    private String imageUrlM;
    private String imageUrlL;
    private PublisherDto publisherDto;
    private CategoryDto categoryDto;
    private double price;
    private String authorName;


}
