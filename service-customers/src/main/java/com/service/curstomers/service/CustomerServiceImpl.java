package com.service.curstomers.service;

import com.service.curstomers.entity.Customer;
import com.service.curstomers.entity.Region;
import com.service.curstomers.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomerServiceImpl implements  CustomerService{

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public List<Customer> getCustomers() {
        return this.customerRepository.findAll();
    }

    @Override
    public Customer getCustomer(Long id) {
        return this.customerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Customer> getCustomerByRegion(Region region) {
        return this.customerRepository.findByRegion(region);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer customerDB = this.customerRepository.findByNumberID(customer.getNumberID());
        if(customerDB != null){
            return customerDB;
        }
        customer.setState("CREATED");
        customerDB = this.customerRepository.save(customer);
        return customerDB;
    }

    @Override
    public Customer updateCustomer(Customer costumer) {
        Customer costumerDB = getCustomer(costumer.getId());
        if(costumerDB == null){
            return null;
        }
        costumerDB.setFirstName(costumer.getFirstName());
        costumerDB.setLastName(costumer.getLastName());
        costumerDB.setEmail(costumer.getEmail());
        costumerDB.setPhotoUrl(costumer.getPhotoUrl());

        return this.customerRepository.save(costumerDB);
    }

    @Override
    public Customer deleteCustomer(Long id) {
        Customer customerDB = getCustomer(id);
        if(customerDB == null){
            return null;
        }
        customerDB.setState("DELETED");
        return this.customerRepository.save(customerDB);
    }

}
