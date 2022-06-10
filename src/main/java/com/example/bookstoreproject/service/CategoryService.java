package com.example.bookstoreproject.service;

import com.example.bookstoreproject.persistance.CategoryRepository;
import com.example.bookstoreproject.persistance.entity.CategoryEntity;
import com.example.bookstoreproject.service.dto.CategoryDto;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    public List<CategoryDto> getAll() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (CategoryEntity category :
                categoryEntities) {
            CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
            categoryDtos.add(categoryDto);
        }
        return categoryDtos;
    }

    public CategoryDto addCategory(CategoryDto categoryDto) {
        categoryRepository.save(modelMapper.map(categoryDto, CategoryEntity.class));
        return categoryDto;
    }

    public CategoryDto getById(int id) {
        Optional<CategoryEntity> byId = categoryRepository.findById(id);
        return modelMapper.map(byId, CategoryDto.class);
    }

    public CategoryDto editCategory(int id, CategoryDto categoryDto) {
        Optional<CategoryEntity> categoryEntity = Optional.of(categoryRepository.findById(id).get());
        categoryDto.setId(id);
        categoryEntity.ifPresent(value -> modelMapper.map(categoryDto, value));
        categoryRepository.save(modelMapper.map(categoryDto, CategoryEntity.class));
        return categoryDto;
    }

    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    public void parseCsv(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setCategory(record[0]);
                categoryDto.setDescription(record[1]);
                categoryRepository.save(modelMapper.map(categoryDto, CategoryEntity.class));
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
