package com.example.bookstoreproject.security;

import com.example.bookstoreproject.service.dto.UserDto;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;


public class CurrentUser extends User {


    private UserDto user;

    public CurrentUser(UserDto user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getUsertype().name()));
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }

}
