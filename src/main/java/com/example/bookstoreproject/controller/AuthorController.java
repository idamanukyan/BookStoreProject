package com.example.bookstoreproject.controller;

import com.example.bookstoreproject.security.CurrentUser;
import com.example.bookstoreproject.service.AuthorService;
import com.example.bookstoreproject.service.dto.UserDto;
import com.example.bookstoreproject.service.dto.AuthorDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        List<AuthorDto> allAuthors = authorService.getAll();
        if (allAuthors.isEmpty()) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(allAuthors);
    }

    @PostMapping("/{id}")
    public ResponseEntity<AuthorDto> addAuthor(@PathVariable(name = "id") int id, @RequestBody AuthorDto author, @AuthenticationPrincipal CurrentUser currentUser) {
        author.setUserDto(modelMapper.map(currentUser.getUser(), UserDto.class));
        AuthorDto authorDto = authorService.addAuthor(modelMapper.map(author, AuthorDto.class));
        if (authorDto.getId() != 0) {
            return ResponseEntity.ok(authorDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable("id") int id) {
        Optional<AuthorDto> authorDtoById = Optional.ofNullable(authorService.getById(id));
        return authorDtoById.map(authorDto ->
                ResponseEntity.ok(authorDto)).orElseGet(() ->
                ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable(name = "id") int id, @RequestBody AuthorDto authorDto) {
        Optional<AuthorDto> authorDtoById = Optional.ofNullable(authorService.getById(id));
        if (!authorDtoById.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        AuthorDto editedAuthorDto = authorService.editAuthor(id, authorDto);
        return ResponseEntity.ok(editedAuthorDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable(name = "id") int id) {
        Optional<AuthorDto> authorDtoById = Optional.ofNullable(authorService.getById(id));
        if (authorDtoById.isPresent()) {
            authorService.deleteAuthor(id);
        }
    }

    @PostMapping("/parseCsv")
    public ResponseEntity<String> parseAuthorCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        authorService.parseCsv(file);
        return ResponseEntity.ok().build();
    }
}
