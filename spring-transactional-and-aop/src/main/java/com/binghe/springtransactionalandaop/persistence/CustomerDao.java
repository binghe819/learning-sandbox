package com.binghe.springtransactionalandaop.persistence;

import com.binghe.springtransactionalandaop.domain.Customer;

import java.util.List;

public interface CustomerDao {
    Long add(Customer customer);
    Customer findById(Long id);
    List<Customer> findAll();
    void update(Customer customer);
    void deleteAll();
}
