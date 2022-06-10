package com.example.bookstoreproject.security;

import com.example.bookstoreproject.persistance.UserRepository;
import com.example.bookstoreproject.service.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private ModelMapper modelMapper;

    public CurrentUserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserDto userDto = modelMapper.map(userRepository.findByEmail(username), UserDto.class);
        //.orElseThrow(() -> new RuntimeException("Message"));
        return new CurrentUser(userDto);
    }
}
