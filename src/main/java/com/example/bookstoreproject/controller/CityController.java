package com.example.bookstoreproject.controller;

import com.example.bookstoreproject.service.CityService;
import com.example.bookstoreproject.service.dto.CityDto;
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
@RequestMapping(value = "/cities")
public class CityController {
    private final CityService cityService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<CityDto>> getAllCities() {
        List<CityDto> allCities = cityService.getAll();
        if (allCities.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allCities);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CityDto> addCity(@PathVariable(name = "id") int id, @RequestBody @Valid CityDto city) {
        CityDto cityDto = cityService.addCity(modelMapper.map(city, CityDto.class));
        if (cityDto.getId() != 0) {
            return ResponseEntity.ok(cityDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCities(@PathVariable("id") int id) {
        Optional<CityDto> byId = Optional.ofNullable(cityService.getById(id));
        return byId.map(cityDto ->
                ResponseEntity.ok(cityDto)).orElseGet(() ->
                ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDto> updateCity(@PathVariable(name = "id") int id, @RequestBody CityDto cityDto) {
        Optional<CityDto> byId = Optional.ofNullable(cityService.getById(id));
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        CityDto city = cityService.editCity(id, cityDto);
        return ResponseEntity.ok(city);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable(name = "id") int id) {
        Optional<CityDto> byId = Optional.ofNullable(cityService.getById(id));
        if (byId.isPresent()) {
            cityService.deleteCity(id);
        }
    }

    @PostMapping("/parseCsv")
    public ResponseEntity<String> parseCityCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        cityService.parseCsv(file);
        return ResponseEntity.ok().build();
    }
}
