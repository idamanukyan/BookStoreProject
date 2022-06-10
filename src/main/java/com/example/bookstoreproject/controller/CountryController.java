package com.example.bookstoreproject.controller;

import com.example.bookstoreproject.service.CountryService;
import com.example.bookstoreproject.service.dto.CountryDto;
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
@RequestMapping(value = "/countries")
public class CountryController {
    private final CountryService countryService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<CountryDto>> getAllCities() {
        List<CountryDto> allCountries = countryService.getAll();
        if (allCountries.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allCountries);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CountryDto> addCountry(@PathVariable(name = "id") int id, @RequestBody @Valid CountryDto country) {
        CountryDto countryDto = countryService.addCountry(modelMapper.map(country, CountryDto.class));
        if (countryDto.getId() != 0) {
            return ResponseEntity.ok(countryDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> getCountries(@PathVariable("id") int id) {
        Optional<CountryDto> byId = Optional.ofNullable(countryService.getById(id));
        return byId.map(countryDto ->
                ResponseEntity.ok(countryDto)).orElseGet(() ->
                ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryDto> updateCountry(@PathVariable(name = "id") int id, @RequestBody CountryDto countryDto) {
        Optional<CountryDto> byId = Optional.ofNullable(countryService.getById(id));
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        CountryDto country = countryService.editCountry(id, countryDto);
        return ResponseEntity.ok(country);
    }

    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable(name = "id") int id) {
        Optional<CountryDto> byId = Optional.ofNullable(countryService.getById(id));
        if (byId.isPresent()) {
            countryService.deleteCountry(id);
        }
    }

    @PostMapping("/parseCsv")
    public ResponseEntity<String> parseCountryCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        countryService.parseCsv(file);
        return ResponseEntity.ok().build();
    }
}
