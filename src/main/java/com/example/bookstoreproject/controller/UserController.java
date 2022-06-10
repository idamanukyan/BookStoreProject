package com.example.bookstoreproject.controller;

import com.example.bookstoreproject.service.UserService;
import com.example.bookstoreproject.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = userService.getAll();
        if (allUsers.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserDto> addUser(@PathVariable(name = "id") int id, @RequestBody @Valid UserDto user) {
        UserDto userDto = userService.addUser(modelMapper.map(user, UserDto.class));
        if (userDto.getId() != 0) {
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") int id) {
        Optional<UserDto> byId = Optional.ofNullable(userService.getById(id));
        return byId.map(userDto ->
                ResponseEntity.ok(userDto)).orElseGet(() ->
                ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") int id, @RequestBody UserDto userDto) {
        Optional<UserDto> byId = Optional.ofNullable(userService.getById(id));
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        UserDto user = userService.editUser(id, userDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(name = "id") int id) {
        Optional<UserDto> byId = Optional.ofNullable(userService.getById(id));
        if (byId.isPresent()) {
            userService.deleteUser(id);
        }
    }

    @PostMapping("/parseCsv")
    public ResponseEntity<String> parseUserCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        userService.parseCsv(file);
        return ResponseEntity.ok().build();
    }
}
