package com.yrgo.services;

import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerManagementServiceProductionImpl;
import com.yrgo.services.customers.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration({"/datasource-test.xml", "/other-tiers.xml"})
@Transactional
public class CustomerManagementIntegrationTests {
    @Autowired
    private CustomerManagementService customers;

    @Test
    public void createNewCustomer() {

        customers.newCustomer(new Customer("1", "company", "email@mail.com", "031121212", "some notes"));

        int existingCustomers = customers.getAllCustomers().size();
        assertEquals(1, existingCustomers, "there should be 1 customer in db");

    }

    @Test
    public void getExistingCustomer() {
        Customer cust1 = new Customer("1", "company", "email@mail.com", "031121212", "some notes");

        customers.newCustomer(cust1);

        try {
           Customer cust2 = customers.findCustomerById("1");
           assertEquals(cust1, cust2, "customer returned is the wrong customer");
        } catch (CustomerNotFoundException e) {
            fail("Could not find customer");
        }
    }
}
