package com.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class PublisherDto {
    private int id;
    private String name;
    private CityDto cityDto;
    private String phoneNumber;
    private String webpageUrl;

}
