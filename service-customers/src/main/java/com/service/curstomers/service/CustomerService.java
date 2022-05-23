package com.service.curstomers.service;

import com.service.curstomers.entity.Customer;
import com.service.curstomers.entity.Region;

import java.util.List;

public interface CustomerService {

    public List<Customer> getCustomers();

    public Customer getCustomer(Long id);

    public List<Customer> getCustomerByRegion(Region region);

    public Customer createCustomer(Customer customer);

    public Customer updateCustomer(Customer customer);

    public Customer deleteCustomer(Long id);

}
