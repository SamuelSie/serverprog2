package com.yrgo.services.customers;

import com.yrgo.dataaccess.CustomerDao;
import com.yrgo.dataaccess.RecordNotFoundException;
import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;

import java.util.List;

public class CustomerManagementServiceProductionImpl implements CustomerManagementService{
    private final CustomerDao customerDao;

    public CustomerManagementServiceProductionImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public void newCustomer(Customer newCustomer) {
        customerDao.create(newCustomer);
    }

    @Override
    public void updateCustomer(Customer changedCustomer) {
        try {
        customerDao.update(changedCustomer);
        } catch (RecordNotFoundException e) {
            System.out.println("Could not find customer to update");
        }
    }

    @Override
    public void deleteCustomer(Customer oldCustomer) {
        try {
        customerDao.delete(oldCustomer);
        } catch (RecordNotFoundException e) {
            System.out.println("Could not delete customer, record not found");
        }
    }

    @Override
    public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
        try {
        return customerDao.getById(customerId);
        } catch (RecordNotFoundException e) {
            System.out.printf("No customer with %s found.", customerId);
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public List<Customer> findCustomersByName(String name) {
        return customerDao.getByName(name);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
        try {
        return customerDao.getFullCustomerDetail(customerId);
        } catch (RecordNotFoundException e) {
            System.out.printf("Could not fetch customer or call data for customer id: %s", customerId);
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
        try {
        customerDao.addCall(callDetails, customerId);
        } catch (RecordNotFoundException e) {
            System.out.println("Could not add call to customer id: " + customerId + "\n" + e);
            throw new CustomerNotFoundException();
        }
    }
}
