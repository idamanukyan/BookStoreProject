package com.example.bookstoreproject.service.dto;

import com.example.bookstoreproject.service.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {

    private int id;
    private int totalBookCount;
    private UserDto userDto;

}
