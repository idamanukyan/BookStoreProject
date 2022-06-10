package com.example.bookstoreproject.service;

import com.example.bookstoreproject.persistance.CreditCardRepository;
import com.example.bookstoreproject.persistance.entity.CreditCardEntity;
import com.example.bookstoreproject.service.dto.CreditCardDto;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final ModelMapper modelMapper;


    public List<CreditCardDto> getAll() {
        List<CreditCardEntity> creditCardEntities = creditCardRepository.findAll();
        List<CreditCardDto> creditCardDtos = new ArrayList<>();
        for (CreditCardEntity creditCardEntity :
                creditCardEntities) {
            CreditCardDto creditCardDto = modelMapper.map(creditCardEntity, CreditCardDto.class);
            creditCardDtos.add(creditCardDto);
        }
        return creditCardDtos;
    }

    public CreditCardDto addCreditCard(CreditCardDto creditCardDto) {
        creditCardRepository.save(modelMapper.map(creditCardDto, CreditCardEntity.class));
        return creditCardDto;
    }

    public CreditCardDto getById(int id) {
        Optional<CreditCardEntity> byId = creditCardRepository.findById(id);
        return modelMapper.map(byId, CreditCardDto.class);
    }

    public CreditCardDto editCreditCard(int id, CreditCardDto creditCardDto) {
        Optional<CreditCardEntity> byId = Optional.of(creditCardRepository.findById(id).get());
        creditCardDto.setId(id);
        byId.ifPresent(value -> modelMapper.map(creditCardDto, value));
        creditCardRepository.save(modelMapper.map(creditCardDto, CreditCardEntity.class));
        return creditCardDto;
    }

    public void deleteCreditCard(int id) {
        creditCardRepository.deleteById(id);
    }

    public void parseCsv(MultipartFile file) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                CreditCardDto creditCardDto = new CreditCardDto();
                creditCardDto.setCardNumber(record[0]);
                creditCardDto.setCardHolderName(record[1]);
                creditCardDto.setCvv(Integer.parseInt(record[2]));
                creditCardRepository.save(modelMapper.map(creditCardDto, CreditCardEntity.class));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

    }
}
