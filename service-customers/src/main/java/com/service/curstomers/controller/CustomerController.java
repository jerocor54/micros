package com.service.curstomers.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.curstomers.entity.Customer;
import com.service.curstomers.entity.Region;
import com.service.curstomers.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers(@RequestParam(name = "idRegion", required = false) Long idRegion){
        List<Customer> customers = new ArrayList<>();
        if(idRegion == null){
            customers = this.customerService.getCustomers();
        }else{
            Region region = new Region();
            region.setId(idRegion);
            customers = this.customerService.getCustomerByRegion(region);
        }

        if(customers.isEmpty()){
            log.error("Customers with Region id {} not found", idRegion);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id){
        log.info("Fetching customer with id {}", id);
        Customer customerDB = this.customerService.getCustomer(id);
        if(customerDB == null){
            log.error("Customer with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerDB);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer, BindingResult result){
        log.info("Creating customer: {}", customer);
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        log.info("Pase el error: {}", customer);
        Customer customerDB = this.customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDB);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customer){
        log.info("Updating customer: {}", customer);
        Customer customerDB = this.customerService.getCustomer(id);
        if(customerDB == null){
            log.error("Customer with id {} not found", id);
            ResponseEntity.notFound().build();
        }
        customer.setId(id);
        customerDB = this.customerService.updateCustomer(customer);
        return ResponseEntity.ok(customerDB);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") Long id){
        log.info("Fetching & Deleting Customer with id {}", id);
        Customer customerDB = this.customerService.getCustomer(id);
        if(customerDB == null){
            log.error("Customer with id {} not found", id);
            ResponseEntity.notFound().build();
        }
        customerDB = customerService.deleteCustomer(id);
        return ResponseEntity.ok(customerDB);
    }

    private String formatMessage(BindingResult result){
        List<Map<String, String>> errors = result.getFieldErrors().stream().map(err -> {
            Map<String, String> error = new HashMap<>();
            error.put(err.getField(), err.getDefaultMessage());
            return error;
        }).collect(Collectors.toList());

        ErrorMessage errorMessage = ErrorMessage.builder().code("01").messages(errors).build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try{
            jsonString = mapper.writeValueAsString(errorMessage);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonString;
    }

}
