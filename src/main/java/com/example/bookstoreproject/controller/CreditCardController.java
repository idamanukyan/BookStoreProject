package com.example.bookstoreproject.controller;

import com.example.bookstoreproject.service.CreditCardService;
import com.example.bookstoreproject.service.dto.CreditCardDto;
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
@RequestMapping(value = "/credit-card")
public class CreditCardController {
    private final CreditCardService creditCardService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<CreditCardDto>> getAllCreditCards() {
        List<CreditCardDto> allCreditCards = creditCardService.getAll();
        if (allCreditCards.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allCreditCards);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CreditCardDto> addCreditCard(@PathVariable(name = "id") int id, @RequestBody @Valid CreditCardDto creditCard) {
        CreditCardDto creditCardDto = creditCardService.addCreditCard(modelMapper.map(creditCard, CreditCardDto.class));
        if (creditCardDto.getId() != 0) {
            return ResponseEntity.ok(creditCardDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCardDto> getCreditCard(@PathVariable("id") int id) {
        Optional<CreditCardDto> byId = Optional.ofNullable(creditCardService.getById(id));
        return byId.map(creditCardDto ->
                ResponseEntity.ok(creditCardDto)).orElseGet(() ->
                ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditCardDto> updateCreditCard(@PathVariable(name = "id") int id, @RequestBody CreditCardDto creditCardDto) {
        Optional<CreditCardDto> byId = Optional.ofNullable(creditCardService.getById(id));
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        CreditCardDto editCreditCard = creditCardService.editCreditCard(id, creditCardDto);
        return ResponseEntity.ok(modelMapper.map(editCreditCard, CreditCardDto.class));
    }

    @DeleteMapping("/{id}")
    public void deleteCreditCard(@PathVariable(name = "id") int id) {
        Optional<CreditCardDto> byId = Optional.ofNullable(creditCardService.getById(id));

        if (byId.isPresent()) {
            creditCardService.deleteCreditCard(id);
        }
    }

    @PostMapping("/parseCsv")
    public ResponseEntity<String> parseCreditCardCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        creditCardService.parseCsv(file);
        return ResponseEntity.ok().build();
    }
}
