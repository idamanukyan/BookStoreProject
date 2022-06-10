package com.example.bookstoreproject.controller;

import com.example.bookstoreproject.service.RatingService;
import com.example.bookstoreproject.service.dto.RatingDto;
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
@RequestMapping(value = "/ratings")
public class RatingController {
    private final RatingService ratingService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<RatingDto>> getAllRatings() {
        List<RatingDto> allRatings = ratingService.getAll();
        if (allRatings.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allRatings);
    }

    @PostMapping("/{id}")
    public ResponseEntity<RatingDto> addRating(@PathVariable(name = "id") int id, @RequestBody @Valid RatingDto rating) {
        RatingDto ratingDto = ratingService.addRating(modelMapper.map(rating, RatingDto.class));
        if (ratingDto.getId() != 0) {
            return ResponseEntity.ok(ratingDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingDto> getRating(@PathVariable("id") int id) {
        Optional<RatingDto> byId = Optional.ofNullable(ratingService.getById(id));
        return byId.map(ratingDto ->
                ResponseEntity.ok(ratingDto)).orElseGet(() ->
                ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RatingDto> updateRating(@PathVariable(name = "id") int id, @RequestBody RatingDto ratingDto) {
        Optional<RatingDto> byId = Optional.ofNullable(ratingService.getById(id));
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        RatingDto rating = ratingService.editRating(id, ratingDto);
        return ResponseEntity.ok(rating);
    }

    @DeleteMapping("/{id}")
    public void deleteRating(@PathVariable(name = "id") int id) {
        Optional<RatingDto> byId = Optional.ofNullable(ratingService.getById(id));
        if (byId.isPresent()) {
            ratingService.deleteBookRating(id);
        }
    }

    @PostMapping("/parseCsv")
    public ResponseEntity<String> parseRatingCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        ratingService.parseCsv(file);
        return ResponseEntity.ok().build();
    }
}
