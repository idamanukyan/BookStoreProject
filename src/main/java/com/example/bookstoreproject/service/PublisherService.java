package com.example.bookstoreproject.service;

import com.example.bookstoreproject.persistance.CityRepository;
import com.example.bookstoreproject.persistance.PublisherRepository;
import com.example.bookstoreproject.persistance.entity.PublisherEntity;
import com.example.bookstoreproject.service.dto.CityDto;
import com.example.bookstoreproject.service.dto.PublisherDto;
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
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;


    public List<PublisherDto> getAll() {
        List<PublisherEntity> publisherEntities = publisherRepository.findAll();
        List<PublisherDto> publisherDtos = new ArrayList<>();
        for (PublisherEntity publisher :
                publisherEntities) {
            PublisherDto publisherDto = modelMapper.map(publisher, PublisherDto.class);
            publisherDtos.add(publisherDto);
        }
        return publisherDtos;
    }

    public PublisherDto addPublisher(PublisherDto publisherDto) {
        publisherRepository.save(modelMapper.map(publisherDto, PublisherEntity.class));
        return publisherDto;
    }

    public PublisherDto getById(int id) {
        Optional<PublisherEntity> byId = publisherRepository.findById(id);
        return modelMapper.map(byId, PublisherDto.class);
    }

    public PublisherDto editPublisher(int id, PublisherDto publisherDto) {
        Optional<PublisherEntity> byId = Optional.of(publisherRepository.findById(id).get());
        publisherDto.setId(id);
        byId.ifPresent(value -> modelMapper.map(publisherDto, value));
        publisherRepository.save(modelMapper.map(publisherDto, PublisherEntity.class));
        return publisherDto;
    }

    public void deletePublisher(int id) {
        publisherRepository.deleteById(id);
    }

    public void parseCsv(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                PublisherDto publisherDto = new PublisherDto();
                publisherDto.setName(record[0]);
                publisherDto.setCityDto(modelMapper.map(cityRepository.findByName(record[1]), CityDto.class));
                publisherDto.setPhoneNumber(record[2]);
                publisherDto.setWebpageUrl(record[3]);
                publisherRepository.save(modelMapper.map(publisherDto, PublisherEntity.class));

            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

    }
}
