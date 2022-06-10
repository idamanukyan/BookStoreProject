package com.example.bookstoreproject.controller;


import com.example.bookstoreproject.service.BookService;
import com.example.bookstoreproject.service.dto.BookDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/books")
public class BookController {

    private final BookService bookService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<Page<BookDto>> getAllBooks(Pageable pageable) {
        Page<BookDto> allBooks = bookService.getAll(pageable);
//        if (allBooks.isEmpty()) {
//            ResponseEntity.noContent().build();
//        }
        return ResponseEntity.ok(allBooks);
    }

    @PostMapping("/{id}")
    public ResponseEntity<BookDto> addBook(@PathVariable(name = "id") int id, @RequestBody @Valid BookDto book) {
        BookDto bookDto = bookService.addBook(modelMapper.map(book, BookDto.class));
        if (bookDto.getId() != 0) {
            return ResponseEntity.ok(bookDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") int id) {
        Optional<BookDto> byId = Optional.ofNullable(bookService.getById(id));
        return byId.map(bookDto ->
                ResponseEntity.ok(bookDto)).orElseGet(() ->
                ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable(name = "id") int id, @RequestBody BookDto bookDto) {
        Optional<BookDto> byId = Optional.ofNullable(bookService.getById(id));
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        BookDto book = bookService.editBook(id, bookDto);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable(name = "id") int id) {
        Optional<BookDto> byId = Optional.ofNullable(bookService.getById(id));
        if (byId.isPresent()) {
            bookService.deleteBook(id);
        }
    }

    @PostMapping("/parseCsv")
    public ResponseEntity<String> parseBookCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        bookService.parseCsv(file);
        return ResponseEntity.ok().build();
    }


}
