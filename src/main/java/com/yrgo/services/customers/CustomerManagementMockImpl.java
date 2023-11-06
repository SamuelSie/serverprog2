package com.yrgo.services.customers;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import com.yrgo.services.customers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerManagementMockImpl implements CustomerManagementService {
    private HashMap<String, Customer> customerMap;

    public CustomerManagementMockImpl() {
        customerMap = new HashMap<String, Customer>();
        customerMap.put("OB74", new Customer("OB74", "Fargo Ltd", "some notes"));
        customerMap.put("NV10", new Customer("NV10", "North Ltd", "some other notes"));
        customerMap.put("RM210", new Customer("RM210", "River Ltd", "some more notes"));
    }


    @Override
    public void newCustomer(Customer newCustomer) {
        customerMap.put(newCustomer.getCustomerId(), newCustomer);
    }

    @Override
    public void updateCustomer(Customer changedCustomer) {
        String customerId = changedCustomer.getCustomerId();
        if (customerMap.containsKey(customerId)) {
            customerMap.put(customerId, changedCustomer);
        }
    }

    @Override
    public void deleteCustomer(Customer oldCustomer) {
        customerMap.remove(oldCustomer.getCustomerId());
    }

    @Override
    public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
        if (customerMap.containsKey(customerId)) {
        return customerMap.get(customerId);
        } else {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public List<Customer> findCustomersByName(String name) {
        List<Customer> customers = new ArrayList<>();
        for (Customer customer : customerMap.values()) {
            if (customer.getCompanyName().equalsIgnoreCase(name)) {
                customers.add(customer);
            }
        }
        return customers;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
        if (customerMap.containsKey(customerId)) {
            return customerMap.get(customerId);
        } else {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
        Customer customer;
        if (customerMap.containsKey(customerId)) {
            customer = customerMap.get(customerId);
        } else {
            throw new CustomerNotFoundException();
        }

        customer.addCall(callDetails);
    }

}
