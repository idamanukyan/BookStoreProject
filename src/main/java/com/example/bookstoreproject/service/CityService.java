package com.example.bookstoreproject.service;

import com.example.bookstoreproject.persistance.CityRepository;
import com.example.bookstoreproject.persistance.CountryRepository;
import com.example.bookstoreproject.persistance.StateRepository;
import com.example.bookstoreproject.persistance.entity.CityEntity;
import com.example.bookstoreproject.service.dto.CityDto;
import com.example.bookstoreproject.service.dto.CountryDto;
import com.example.bookstoreproject.service.dto.StateDto;
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
public class CityService {
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;


    public List<CityDto> getAll() {
        List<CityEntity> cityEntities = cityRepository.findAll();
        List<CityDto> cityDtos = new ArrayList<>();
        for (CityEntity city :
                cityEntities) {
            CityDto cityDto = modelMapper.map(city, CityDto.class);
            cityDtos.add(cityDto);
        }
        return cityDtos;
    }

    public CityDto addCity(CityDto cityDto) {
        cityRepository.save(modelMapper.map(cityDto, CityEntity.class));
        return cityDto;
    }

    public CityDto getById(int id) {
        Optional<CityEntity> byId = cityRepository.findById(id);
        return modelMapper.map(byId, CityDto.class);
    }

    public CityDto editCity(int id, CityDto cityDto) {
        Optional<CityEntity> cityEntity = Optional.of(cityRepository.findById(id).get());
        cityDto.setId(id);
        cityEntity.ifPresent(value -> modelMapper.map(cityDto, value));
        cityRepository.save(modelMapper.map(cityDto, CityEntity.class));
        return cityDto;
    }

    public void deleteCity(int id) {
        cityRepository.deleteById(id);
    }

    public void parseCsv(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                CityDto cityDto = new CityDto();
                cityDto.setName(record[0]);
                if (stateRepository.findByNameAndCountry_Name(record[1], record[2]).isPresent()) {
                    cityDto.setStateDto(modelMapper.map(stateRepository.findByNameAndCountry_Name(record[1], record[2]).get(), StateDto.class));
                } else {
                    cityDto.setStateDto(null);
                }
                if (countryRepository.findByName(record[2]).isPresent()) {
                    cityDto.setCountryDto(modelMapper.map(countryRepository.findByName(record[2]).get(), CountryDto.class));
                } else {
                    cityDto.setStateDto(null);
                }
                cityRepository.save(modelMapper.map(cityDto, CityEntity.class));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }


    }
}
