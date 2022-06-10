package com.example.bookstoreproject.controller;

import com.example.bookstoreproject.service.PublisherService;
import com.example.bookstoreproject.service.dto.PublisherDto;
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
@RequestMapping(value = "/publishers")
public class PublisherController {
    private final PublisherService publisherService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<PublisherDto>> getAllPublishers() {
        List<PublisherDto> allPublishers = publisherService.getAll();
        if (allPublishers.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allPublishers);
    }

    @PostMapping("/{id}")
    public ResponseEntity<PublisherDto> addPublisher(@PathVariable(name = "id") int id, @RequestBody @Valid PublisherDto publisher) {
        PublisherDto publisherDto = publisherService.addPublisher(modelMapper.map(publisher, PublisherDto.class));
        if (publisherDto.getId() != 0) {
            return ResponseEntity.ok(publisherDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDto> getPublisher(@PathVariable("id") int id) {
        Optional<PublisherDto> byId = Optional.ofNullable(publisherService.getById(id));
        return byId.map(publisherDto ->
                ResponseEntity.ok(publisherDto)).orElseGet(() ->
                ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherDto> updatePublisher(@PathVariable(name = "id") int id, @RequestBody PublisherDto publisherDto) {
        Optional<PublisherDto> byId = Optional.ofNullable(publisherService.getById(id));
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        PublisherDto editPublisher = publisherService.editPublisher(id, publisherDto);
        return ResponseEntity.ok(editPublisher);
    }

    @DeleteMapping("/{id}")
    public void deletePublisher(@PathVariable(name = "id") int id) {
        Optional<PublisherDto> byId = Optional.ofNullable(publisherService.getById(id));
        if (byId.isPresent()) {
            publisherService.deletePublisher(id);
        }
    }

    @PostMapping("/parseCsv")
    public ResponseEntity<String> parsePublisherCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        publisherService.parseCsv(file);
        return ResponseEntity.ok().build();
    }
}
