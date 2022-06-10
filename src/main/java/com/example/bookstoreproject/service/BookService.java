package com.example.bookstoreproject.service;

import com.example.bookstoreproject.persistance.BookRepository;
import com.example.bookstoreproject.persistance.CategoryRepository;
import com.example.bookstoreproject.persistance.PublisherRepository;
import com.example.bookstoreproject.persistance.entity.BookEntity;
import com.example.bookstoreproject.service.dto.BookDto;
import com.example.bookstoreproject.service.dto.CategoryDto;
import com.example.bookstoreproject.service.dto.PublisherDto;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Value("${file.upload.dir}")
    private String uploadDir;

    public Page<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookEntity -> modelMapper.map(bookEntity, BookDto.class));
    }

    public BookDto addBook(BookDto bookDto) {
        bookRepository.save(modelMapper.map(bookDto, BookEntity.class));
        return bookDto;
    }

    public BookDto getById(int id) {
        return bookRepository.findById(id)
                .map(bookEntity -> modelMapper.map(bookEntity, BookDto.class)).get();
//                .orElseThrow(new Exception("sds"));
//        Optional<BookEntity> byId = bookRepository.findById(id);
//        return modelMapper.map(byId, BookDto.class);
    }

    public BookDto editBook(int id, BookDto bookDto) {
        Optional<BookEntity> byId = bookRepository.findById(id);
        bookDto.setId(id);
        byId.ifPresent(value -> modelMapper.map(bookDto, value));
        bookRepository.save(modelMapper.map(bookDto, BookEntity.class));
        return bookDto;
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    public void parseCsv(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                BookDto bookDto = new BookDto();
                bookDto.setPublisherDto(modelMapper.map(publisherRepository.findByPhoneNumber(record[0]), PublisherDto.class));
                bookDto.setCategoryDto(modelMapper.map(categoryRepository.findByCategory(record[1]), CategoryDto.class));
                bookDto.setPrice(Double.parseDouble(record[2]));
                bookDto.setIsbn(Integer.parseInt(record[3]));
                bookDto.setTitle(record[4]);
                LocalDate date = LocalDate.parse(record[5]);
                bookDto.setPublicationDate(date);
                bookDto.setImageUrlS(record[6]); //not included in csv
                bookDto.setImageUrlM(record[7]);
                bookDto.setImageUrlL(record[8]);
                bookDto.setAuthorName(record[9]);
                bookRepository.save(modelMapper.map(bookDto, BookEntity.class));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

    }

    @Scheduled(fixedRate = 2000)
    public void downloadPhoto() {
        List<String> photoUrl = bookRepository.getUrls();
        try {
            for (String url :
                    photoUrl) {
                //update image status -> download process is in progress
                InputStream inputStream = new URL(url).openStream();
                Files.copy(inputStream, Paths.get(url + ".jpg"), StandardCopyOption.REPLACE_EXISTING);

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            //set status failed
        }
        //update image status -> downloaded
        //set that the download process went successfully
    }
}
