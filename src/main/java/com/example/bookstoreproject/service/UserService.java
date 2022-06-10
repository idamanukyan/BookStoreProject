package com.example.bookstoreproject.service;

import com.example.bookstoreproject.persistance.CityRepository;
import com.example.bookstoreproject.persistance.UserRepository;
import com.example.bookstoreproject.persistance.entity.UserEntity;
import com.example.bookstoreproject.persistance.entity.UserType;
import com.example.bookstoreproject.service.dto.CityDto;
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
public class UserService {
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;


    public List<UserDto> getAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (UserEntity user :
                userEntities) {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    public UserDto addUser(UserDto userDto) {
        userRepository.save(modelMapper.map(userDto, UserEntity.class));
        return userDto;
    }

    public UserDto getById(int id) {
        Optional<UserEntity> byId = userRepository.findById(id);
        return modelMapper.map(byId, UserDto.class);
    }

    public UserDto editUser(int id, UserDto userDto) {
        Optional<UserEntity> byId = Optional.of(userRepository.findById(id).get());
        userDto.setId(id);
        byId.ifPresent(value -> modelMapper.map(userDto, value));
        userRepository.save(modelMapper.map(userDto, UserEntity.class));
        return userDto;
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public UserDto findUserByEmail(String email) {
        Optional<UserEntity> userEntityByEmail = userRepository.findUserEntityByEmail(email);
        return modelMapper.map(userEntityByEmail, UserDto.class);
    }

    public void parseCsv(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                UserDto userDto = new UserDto();
                userDto.setName(record[0]);
                userDto.setSurname(record[1]);
                userDto.setAge(Integer.parseInt(record[2]));
                userDto.setEmail(record[3]);
                userDto.setPassword(record[4]);
                userDto.setUsertype(UserType.valueOf(record[5]));
                userDto.setCityDto(modelMapper.map(cityRepository.findByName(record[6]), CityDto.class));
                userDto.setHasMembershipCard(Boolean.parseBoolean(record[7]));
                userRepository.save(modelMapper.map(userDto, UserEntity.class));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

    }
}
