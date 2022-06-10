package com.example.bookstoreproject.service;

import com.example.bookstoreproject.persistance.CustomerRepository;
import com.example.bookstoreproject.persistance.UserRepository;
import com.example.bookstoreproject.persistance.entity.CustomerEntity;
import com.example.bookstoreproject.service.dto.CustomerDto;
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

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public List<CustomerDto> getAll() {
        List<CustomerEntity> customerEntities = customerRepository.findAll();
        List<CustomerDto> customerDtos = new ArrayList<>();
        for (CustomerEntity customer :
                customerEntities) {
            CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
            customerDtos.add(customerDto);
        }
        return customerDtos;
    }

    public CustomerDto addCustomer(CustomerDto customerDto) {
        customerRepository.save(modelMapper.map(customerDto, CustomerEntity.class));
        return customerDto;
    }

    public CustomerDto getById(int id) {
        Optional<CustomerEntity> byId = customerRepository.findById(id);
        return modelMapper.map(byId, CustomerDto.class);
    }

    public CustomerDto editCustomer(int id, CustomerDto customerDto) {
        Optional<CustomerEntity> customerEntity = Optional.of(customerRepository.findById(id).get());
        customerDto.setId(id);
        customerEntity.ifPresent(value -> modelMapper.map(customerDto, value));
        customerRepository.save(modelMapper.map(customerDto, CustomerEntity.class));
        return customerDto;
    }

    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }

    public void parseCsv(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                CustomerDto customerDto = new CustomerDto();
                customerDto.setPostalCode(record[0]);
                customerDto.setPhoneNumber(record[1]);
                customerDto.setUserDto(modelMapper.map(customerRepository.findCustomerEntitiesByUser_Email(record[2]), UserDto.class));
                customerRepository.save(modelMapper.map(customerDto, CustomerEntity.class));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

    }
}
