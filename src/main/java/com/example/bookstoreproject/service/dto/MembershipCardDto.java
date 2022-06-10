package com.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class MembershipCardDto {
    private int id;
    private UserDto userDto;
    private LocalDate expirationDate;
    private CreditCardDto creditCardDto;


}
