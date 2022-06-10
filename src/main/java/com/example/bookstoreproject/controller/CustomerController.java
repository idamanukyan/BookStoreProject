package com.example.bookstoreproject.controller;

import com.example.bookstoreproject.service.CustomerService;
import com.example.bookstoreproject.service.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> allCustomers = customerService.getAll();
        if (allCustomers.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allCustomers);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CustomerDto> addCustomer(@PathVariable(name = "id") int id, @RequestBody @Valid CustomerDto customer) {
        CustomerDto customerDto = customerService.addCustomer(modelMapper.map(customer, CustomerDto.class));
        if (customerDto.getId() != 0) {
            return ResponseEntity.ok(customerDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("id") int id) {
        Optional<CustomerDto> byId = Optional.ofNullable(customerService.getById(id));
        return byId.map(customerDto ->
                ResponseEntity.ok(customerDto)).orElseGet(() ->
                ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable(name = "id") int id, @RequestBody CustomerDto customerDto) {
        Optional<CustomerDto> byId = Optional.ofNullable(customerService.getById(id));
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        CustomerDto customer = customerService.editCustomer(id, customerDto);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable(name = "id") int id) {
        Optional<CustomerDto> byId = Optional.ofNullable(customerService.getById(id));
        if (byId.isPresent()) {
            customerService.deleteCustomer(id);
        }
    }

    @PostMapping("/parseCsv")
    public ResponseEntity<String> parseCustomerCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        customerService.parseCsv(file);
        return ResponseEntity.ok().build();
    }
}
