package com.example.bookstoreproject.service;


import com.example.bookstoreproject.persistance.BookRepository;
import com.example.bookstoreproject.persistance.RatingRepository;
import com.example.bookstoreproject.persistance.UserRepository;
import com.example.bookstoreproject.persistance.entity.RatingEntity;
import com.example.bookstoreproject.service.dto.BookDto;
import com.example.bookstoreproject.service.dto.RatingDto;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;


    public List<RatingDto> getAll() {
        List<RatingEntity> ratingEntities = ratingRepository.findAll();
        List<RatingDto> ratingDtos = new ArrayList<>();
        for (RatingEntity rating :
                ratingEntities) {
            RatingDto ratingDto = modelMapper.map(rating, RatingDto.class);
            ratingDtos.add(ratingDto);
        }
        return ratingDtos;
    }

    public RatingDto addRating(RatingDto ratingDto) {
        ratingRepository.save(modelMapper.map(ratingDto, RatingEntity.class));
        return ratingDto;
    }

    public RatingDto getById(int id) {
        Optional<RatingEntity> byId = ratingRepository.findById(id);
        return modelMapper.map(byId, RatingDto.class);
    }

    public RatingDto editRating(int id, RatingDto ratingDto) {
        Optional<RatingEntity> byId = Optional.of(ratingRepository.findById(id).get());
        ratingDto.setId(id);
        byId.ifPresent(value -> modelMapper.map(ratingDto, value));
        ratingRepository.save(modelMapper.map(ratingDto, RatingEntity.class));
        return ratingDto;
    }

    public void deleteBookRating(int id) {
        ratingRepository.deleteById(id);
    }

    public void parseCsv(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                RatingDto ratingDto = new RatingDto();
                ratingDto.setUserDto(modelMapper.map(userRepository.findUserEntityByEmail(record[0]), UserDto.class));
                ratingDto.setBookDto(modelMapper.map(bookRepository.findByIsbn(Integer.parseInt(record[1])), BookDto.class));
                ratingDto.setRating(Integer.parseInt(record[2]));
                ratingDto.setFeedback(record[3]);
                ratingRepository.save(modelMapper.map(ratingDto, RatingEntity.class));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

    }
}
