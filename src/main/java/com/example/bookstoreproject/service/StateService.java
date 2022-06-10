package com.example.bookstoreproject.service;

import com.example.bookstoreproject.persistance.CountryRepository;
import com.example.bookstoreproject.persistance.StateRepository;
import com.example.bookstoreproject.persistance.entity.StateEntity;
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
public class StateService {
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;


    public List<StateDto> getAll() {
        List<StateEntity> stateEntities = stateRepository.findAll();
        List<StateDto> stateDtos = new ArrayList<>();
        for (StateEntity state :
                stateEntities) {
            StateDto stateDto = modelMapper.map(state, StateDto.class);
            stateDtos.add(stateDto);
        }
        return stateDtos;
    }

    public StateDto addState(StateDto stateDto) {
        stateRepository.save(modelMapper.map(stateDto, StateEntity.class));
        return stateDto;
    }

    public StateDto getById(int id) {
        Optional<StateEntity> byId = stateRepository.findById(id);
        return modelMapper.map(byId, StateDto.class);
    }

    public StateDto editState(int id, StateDto stateDto) {
        Optional<StateEntity> stateEntity = Optional.of(stateRepository.findById(id).get());
        stateDto.setId(id);
        stateEntity.ifPresent(value -> modelMapper.map(stateDto, value));
        stateRepository.save(modelMapper.map(stateDto, StateEntity.class));

        return stateDto;
    }

    public void deleteState(int id) {
        stateRepository.deleteById(id);
    }

    public void parseCsv(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                StateDto stateDto = new StateDto();
                stateDto.setName(record[0]);
                if (countryRepository.findByName(record[1]).isPresent()) {
                    stateDto.setCountryDto(modelMapper.map(countryRepository.findByName(record[1]).get(), CountryDto.class));
                } else {
                    stateDto.setCountryDto(null);
                }
                stateRepository.save(modelMapper.map(stateDto, StateEntity.class));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }


    }
}
