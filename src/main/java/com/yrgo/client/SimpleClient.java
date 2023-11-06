package com.yrgo.client;

import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerManagementMockImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class SimpleClient {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml");
        CustomerManagementMockImpl cms = container.getBean(CustomerManagementMockImpl.class);
        List<Customer> allCustomers = cms.getAllCustomers();

        for (Customer customer : allCustomers) {
            System.out.println(customer.toString());
        }
        container.close();
    }
}
