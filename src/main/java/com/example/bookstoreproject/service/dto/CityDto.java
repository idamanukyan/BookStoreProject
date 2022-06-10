package com.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class CityDto {
    private int id;
    private String name;
    private StateDto stateDto;
    private CountryDto countryDto;
}
