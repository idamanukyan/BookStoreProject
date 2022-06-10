package com.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class CreditCardDto {
    private int id;
    private String cardNumber;
    private String cardHolderName;
    private int cvv;
}
