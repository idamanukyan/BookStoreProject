package com.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class RatingDto {

    private int id;
    private UserDto userDto;
    private BookDto bookDto;
    private int rating;
    private String feedback;

}
