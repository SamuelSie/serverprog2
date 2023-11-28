package com.yrgo.dataaccess;

import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("customerDao")
public class CustomerDaoJpaImpl implements CustomerDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Customer customer) {
        em.persist(customer);
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        return (Customer) em.createQuery("select c from Customer c where c.customerId=:customerId").setParameter("customerId", customerId).getSingleResult();
    }

    @Override
    public List<Customer> getByName(String name) {
        return em.createQuery("select c from Customer c where c.companyName=:name").setParameter("name", name).getResultList();
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        Customer existingCustomer = em.find(Customer.class, customerToUpdate.getCustomerId());
        if (existingCustomer == null) {
            throw new RecordNotFoundException();
        }
        em.merge(customerToUpdate);
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        Customer customer = em.find(Customer.class, oldCustomer);
        if (customer == null) {
            throw new RecordNotFoundException();
        }
        em.remove(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return em.createQuery("select c from Customer c", Customer.class).getResultList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        List<Object> fullCustomerDetails = em.createQuery("select customer, call from Customer as customer left join Call as call where customer.customerId =:customerId").setParameter("customerId", customerId).getResultList();
        return null;
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        Customer customer = em.createQuery("select c from Customer c where c.customerId =: customerId", Customer.class).setParameter("customerId", customerId).getSingleResult();

        customer.addCall(newCall);
        em.merge(customer);
    }
}
