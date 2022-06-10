package com.example.bookstoreproject.service;

import com.example.bookstoreproject.persistance.CountryRepository;
import com.example.bookstoreproject.persistance.entity.CountryEntity;
import com.example.bookstoreproject.service.dto.CountryDto;
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
public class CountryService {
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;


    public List<CountryDto> getAll() {
        List<CountryEntity> countryEntities = countryRepository.findAll();
        List<CountryDto> countryDtos = new ArrayList<>();
        for (CountryEntity country :
                countryEntities) {
            CountryDto countryDto = modelMapper.map(country, CountryDto.class);
            countryDtos.add(countryDto);
        }
        return countryDtos;
    }

    public CountryDto addCountry(CountryDto countryDto) {
        countryRepository.save(modelMapper.map(countryDto, CountryEntity.class));
        return countryDto;
    }

    public CountryDto getById(int id) {
        Optional<CountryEntity> byId = countryRepository.findById(id);
        return modelMapper.map(byId, CountryDto.class);
    }

    public CountryDto editCountry(int id, CountryDto countryDto) {
        Optional<CountryEntity> countryEntity = Optional.of(countryRepository.findById(id).get());
        countryDto.setId(id);
        countryEntity.ifPresent(value -> modelMapper.map(countryDto, value));
        countryRepository.save(modelMapper.map(countryDto, CountryEntity.class));
        return countryDto;
    }

    public void deleteCountry(int id) {
        countryRepository.deleteById(id);
    }

    public void parseCsv(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                CountryDto countryDto = new CountryDto();
                countryDto.setName(record[0]);
                countryRepository.save(modelMapper.map(countryDto, CountryEntity.class));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }


    }
}
