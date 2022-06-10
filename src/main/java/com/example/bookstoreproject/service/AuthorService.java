package com.example.bookstoreproject.service;

import com.example.bookstoreproject.persistance.AuthorRepository;
import com.example.bookstoreproject.persistance.UserRepository;
import com.example.bookstoreproject.persistance.entity.AuthorEntity;
import com.example.bookstoreproject.service.dto.AuthorDto;
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
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<AuthorDto> getAll() {
        List<AuthorEntity> allAuthorEntities = authorRepository.findAll();
        List<AuthorDto> allAuthorDtos = new ArrayList<>();
        for (AuthorEntity authorEntity :
                allAuthorEntities) {
            AuthorDto authorDto = modelMapper.map(authorEntity, AuthorDto.class);
            allAuthorDtos.add(authorDto);
        }
        return allAuthorDtos;
    }

    public AuthorDto addAuthor(AuthorDto authorDto) {
        authorRepository.save(modelMapper.map(authorDto, AuthorEntity.class));
        return authorDto;
    }

    public AuthorDto getById(int id) {
        Optional<AuthorEntity> byId = authorRepository.findById(id);
        return modelMapper.map(byId, AuthorDto.class);
    }

    public AuthorDto editAuthor(int id, AuthorDto authorDto) {
        Optional<AuthorEntity> authorById = authorRepository.findById(id);
        authorDto.setId(id);
        authorById.ifPresent(value -> modelMapper.map(authorDto, value));
        authorRepository.save(modelMapper.map(authorDto, AuthorEntity.class));
        return authorDto;
    }

    public void deleteAuthor(int id) {
        authorRepository.deleteById(id);
    }

    public void parseCsv(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                AuthorDto authorDto = new AuthorDto();
                String totalBookCount = record[0];
                if (totalBookCount.isEmpty()) {
                    authorDto.setTotalBookCount(0);
                } else {
                    authorDto.setTotalBookCount(Integer.parseInt(totalBookCount));
                }
                String userId = record[1];
                if (userId.isEmpty()) {
                    authorDto.setUserDto(new UserDto());
                } else {
                    authorDto.setUserDto(modelMapper.map(userRepository.getById(Integer.valueOf(userId)), UserDto.class));
                }
                authorRepository.save(modelMapper.map(authorDto, AuthorEntity.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

    }

}

