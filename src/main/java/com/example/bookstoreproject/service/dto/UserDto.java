package com.example.bookstoreproject.service.dto;

import com.example.bookstoreproject.persistance.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class UserDto {
    private int id;
    private String name;
    private String surname;
    private int age;
    private String email;
    private String password;
    private UserType usertype;
    private CityDto cityDto;
    private boolean hasMembershipCard;

}
