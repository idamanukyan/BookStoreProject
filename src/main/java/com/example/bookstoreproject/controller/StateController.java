package com.example.bookstoreproject.controller;

import com.example.bookstoreproject.service.StateService;
import com.example.bookstoreproject.service.dto.StateDto;
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
@RequestMapping(value = "/states")
public class StateController {
    private final StateService stateService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<StateDto>> getAllStates() {
        List<StateDto> allStates = stateService.getAll();
        if (allStates.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allStates);
    }

    @PostMapping("/{id}")
    public ResponseEntity<StateDto> addState(@PathVariable(name = "id") int id, @RequestBody @Valid StateDto state) {
        StateDto stateDto = stateService.addState(modelMapper.map(state, StateDto.class));
        if (stateDto.getId() != 0) {
            return ResponseEntity.ok(stateDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StateDto> getStates(@PathVariable("id") int id) {
        Optional<StateDto> byId = Optional.ofNullable(stateService.getById(id));
        return byId.map(stateDto ->
                ResponseEntity.ok(stateDto)).orElseGet(() ->
                ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StateDto> updateState(@PathVariable(name = "id") int id, @RequestBody StateDto stateDto) {
        Optional<StateDto> byId = Optional.ofNullable(stateService.getById(id));
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        StateDto state = stateService.editState(id, stateDto);
        return ResponseEntity.ok(state);
    }

    @DeleteMapping("/{id}")
    public void deleteState(@PathVariable(name = "id") int id) {
        Optional<StateDto> byId = Optional.ofNullable(stateService.getById(id));
        if (byId.isPresent()) {
            stateService.deleteState(id);
        }
    }

    @PostMapping("/parseCsv")
    public ResponseEntity<String> parseStateCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        stateService.parseCsv(file);
        return ResponseEntity.ok().build();
    }
}
