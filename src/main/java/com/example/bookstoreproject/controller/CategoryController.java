package com.example.bookstoreproject.controller;

import com.example.bookstoreproject.service.CategoryService;
import com.example.bookstoreproject.service.dto.CategoryDto;
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
@RequestMapping(value = "/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> allCategories = categoryService.getAll();
        if (allCategories.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allCategories);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CategoryDto> addCategory(@PathVariable(name = "id") int id, @RequestBody @Valid CategoryDto category) {
        CategoryDto categoryDto = categoryService.addCategory(modelMapper.map(category, CategoryDto.class));
        if (categoryDto.getId() != 0) {
            return ResponseEntity.ok(categoryDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategories(@PathVariable("id") int id) {
        Optional<CategoryDto> byId = Optional.ofNullable(categoryService.getById(id));
        return byId.map(categoryDto ->
                ResponseEntity.ok(categoryDto)).orElseGet(() ->
                ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable(name = "id") int id, @RequestBody CategoryDto categoryDto) {
        Optional<CategoryDto> byId = Optional.ofNullable(categoryService.getById(id));
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        CategoryDto category = categoryService.editCategory(id, categoryDto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable(name = "id") int id) {
        Optional<CategoryDto> byId = Optional.ofNullable(categoryService.getById(id));
        if (byId.isPresent()) {
            categoryService.deleteCategory(id);
        }
    }

    @PostMapping("/parseCsv")
    public ResponseEntity<String> parseCategoryCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        categoryService.parseCsv(file);
        return ResponseEntity.ok().build();
    }
}
