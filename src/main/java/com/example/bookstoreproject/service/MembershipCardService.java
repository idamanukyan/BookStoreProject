package com.example.bookstoreproject.service;

import com.example.bookstoreproject.persistance.CreditCardRepository;
import com.example.bookstoreproject.persistance.MembershipCardRepository;
import com.example.bookstoreproject.persistance.UserRepository;
import com.example.bookstoreproject.persistance.entity.CreditCardEntity;
import com.example.bookstoreproject.persistance.entity.MembershipCardEntity;
import com.example.bookstoreproject.service.dto.CreditCardDto;
import com.example.bookstoreproject.service.dto.MembershipCardDto;
import com.example.bookstoreproject.service.dto.UserDto;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembershipCardService {
    private final MembershipCardRepository membershipCardRepository;
    private final UserRepository userRepository;
    private final CreditCardRepository creditCardRepository;
    private final ModelMapper modelMapper;


    public List<MembershipCardDto> getAll() {
        List<MembershipCardEntity> membershipCardEntities = membershipCardRepository.findAll();
        List<MembershipCardDto> membershipCardDtos = new ArrayList<>();
        for (MembershipCardEntity membershipCard :
                membershipCardEntities) {
            MembershipCardDto membershipCardDto = modelMapper.map(membershipCard, MembershipCardDto.class);
            membershipCardDtos.add(membershipCardDto);
        }
        return membershipCardDtos;
    }

    public MembershipCardDto addMembershipCard(MembershipCardDto membershipCardDto) {
        membershipCardRepository.save(modelMapper.map(membershipCardDto, MembershipCardEntity.class));
        return membershipCardDto;
    }

    public MembershipCardDto getById(int id) {
        Optional<MembershipCardEntity> byId = membershipCardRepository.findById(id);
        return modelMapper.map(byId, MembershipCardDto.class);
    }

    public MembershipCardDto editMembershipCard(int id, MembershipCardDto membershipCardDto) {
        Optional<MembershipCardEntity> byId = Optional.of(membershipCardRepository.findById(id).get());
        membershipCardDto.setId(id);
        byId.ifPresent(value -> modelMapper.map(membershipCardDto, MembershipCardEntity.class));
        membershipCardRepository.save(modelMapper.map(membershipCardDto, MembershipCardEntity.class));
        return membershipCardDto;
    }

    public void deleteMembershipCard(int id) {
        membershipCardRepository.deleteById(id);
    }

    public void parseCsv(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                MembershipCardDto membershipCardDto = new MembershipCardDto();
                membershipCardDto.setUserDto(modelMapper.map(userRepository.findUserEntityByEmail(record[0]), UserDto.class));
                LocalDate date = LocalDate.parse(record[1]);
                membershipCardDto.setExpirationDate(date);
                Optional<CreditCardEntity> byCardNumber = creditCardRepository.findByCardNumber(record[2]);
                membershipCardDto.setCreditCardDto(modelMapper.map(byCardNumber, CreditCardDto.class));
                membershipCardRepository.save(modelMapper.map(membershipCardDto, MembershipCardEntity.class));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

    }
}
