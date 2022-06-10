package com.example.bookstoreproject.controller;

import com.example.bookstoreproject.service.MembershipCardService;
import com.example.bookstoreproject.service.dto.MembershipCardDto;
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
@RequestMapping(value = "/membership-card")
public class MembershipCardController {
    private final MembershipCardService membershipCardService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<MembershipCardDto>> getAllMembershipCards() {
        List<MembershipCardDto> allMembershipCards = membershipCardService.getAll();
        if (allMembershipCards.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allMembershipCards);
    }

    @PostMapping("/{id}")
    public ResponseEntity<MembershipCardDto> addMembershipCard(@PathVariable(name = "id") int id, @RequestBody @Valid MembershipCardDto membershipCard) {
        MembershipCardDto membershipCardDto = membershipCardService.addMembershipCard(modelMapper.map(membershipCard, MembershipCardDto.class));
        if (membershipCardDto.getId() != 0) {
            return ResponseEntity.ok(membershipCardDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipCardDto> getMembershipCard(@PathVariable("id") int id) {
        Optional<MembershipCardDto> byId = Optional.ofNullable(membershipCardService.getById(id));
        return byId.map(membershipCardDto ->
                ResponseEntity.ok(membershipCardDto)).orElseGet(() ->
                ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipCardDto> updateMembershipCard(@PathVariable(name = "id") int id, @RequestBody MembershipCardDto membershipCardDto) {
        Optional<MembershipCardDto> byId = Optional.ofNullable(membershipCardService.getById(id));
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        MembershipCardDto membershipCard = membershipCardService.editMembershipCard(id, membershipCardDto);
        return ResponseEntity.ok(membershipCard);
    }

    @DeleteMapping("/{id}")
    public void deleteMembershipCard(@PathVariable(name = "id") int id) {
        Optional<MembershipCardDto> byId = Optional.ofNullable(membershipCardService.getById(id));
        if (byId.isPresent()) {
            membershipCardService.deleteMembershipCard(id);
        }
    }

    @PostMapping("/parseCsv")
    public ResponseEntity<String> parseMembershipCardCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        membershipCardService.parseCsv(file);
        return ResponseEntity.ok().build();
    }
}
