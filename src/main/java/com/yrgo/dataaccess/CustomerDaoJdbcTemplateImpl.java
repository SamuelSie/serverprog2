package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CustomerDaoJdbcTemplateImpl implements CustomerDao {
    private static final String ADD_CUSTOMER = "INSERT INTO customer (COMPANY_NAME, EMAIL, TELEPHONE, NOTES) VALUES(?,?,?,?)";
    private static final String GET_CUSTOMER_BY_ID = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID=?";
    private static final String GET_CUSTOMER_BY_NAME = "SELECT * FROM CUSTOMER WHERE COMPANY_NAME=?";
    private static final String UPDATE_CUSTOMER = "UPDATE customer SET COMPANY_NAME=?, EMAIL=?, TELEPHONE=?, NOTES=? WHERE CUSTOMER_ID=?";
    private static final String DELETE_CUSTOMER = "DELETE FROM customer WHERE CUSTOMER_ID=?";
    private static final String GET_ALL_CUSTOMERS = "SELECT * FROM CUSTOMER";
    private static final String ADD_CALL = "INSERT INTO TBL_CALL (TIME_AND_DATE, NOTES, CUSTOMER_ID) VALUES(?,?,?)";
    private static final String GET_CALL_BY_CUSTOMER = "SELECT * FROM TBL_CALL WHERE CUSTOMER_ID=?";

    private JdbcTemplate template;


    public CustomerDaoJdbcTemplateImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void create(Customer customer) {
        try {
            this.template.update(ADD_CUSTOMER, customer.getCompanyName(), customer.getEmail(), customer.getTelephone(), customer.getNotes());

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        try {
            return this.template.queryForObject(GET_CUSTOMER_BY_ID, new CustomerRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getByName(String name) {
        return this.template.query(GET_CUSTOMER_BY_NAME, new CustomerRowMapper(), name);
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        try {
            this.template.update(UPDATE_CUSTOMER, customerToUpdate.getCompanyName(), customerToUpdate.getEmail(), customerToUpdate.getTelephone(), customerToUpdate.getNotes(), customerToUpdate.getCustomerId());
        } catch (DataAccessException e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        try {
            this.template.update(DELETE_CUSTOMER, oldCustomer.getCustomerId());
        } catch (DataAccessException e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return this.template.query(GET_ALL_CUSTOMERS, new CustomerRowMapper());
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        try {
            Customer customer = this.template.queryForObject(GET_CUSTOMER_BY_ID, new CustomerRowMapper(), customerId);
            List<Call> calls = this.template.query(GET_CALL_BY_CUSTOMER, new CallRowMapper(), customerId);
            customer.setCalls(calls);

            return customer;
        } catch (DataAccessException e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        try {
            this.template.update(ADD_CALL, newCall.getTimeAndDate(), newCall.getNotes(), customerId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RecordNotFoundException();
        }
    }

    @PostConstruct
    private void createTables() {
        try {
            // IF NOT EXISTS does not work. Db has to be removed between runs.
            this.template.update("CREATE TABLE CUSTOMER (CUSTOMER_ID VARCHAR(10), COMPANY_NAME VARCHAR(255), EMAIL VARCHAR(70), TELEPHONE VARCHAR(20), NOTES VARCHAR(255))");
            this.template.update("CREATE TABLE TBL_CALL (TIME_AND_DATE DATE, NOTES VARCHAR(255), CUSTOMER_ID VARCHAR(50))");

        } catch (BadSqlGrammarException e) {
            System.err.println("Table already exists");
        }
    }
}

class CustomerRowMapper implements RowMapper<Customer> {
    public Customer mapRow(ResultSet rs, int arg1) throws SQLException {
        String customerId = rs.getString("CUSTOMER_ID");
        String companyName = rs.getString("COMPANY_NAME");
        String email = rs.getString("EMAIL");
        String telephone = rs.getString("TELEPHONE");
        String notes = rs.getString("NOTES");

        return new Customer(customerId, companyName, email, telephone, notes);
    }
}

class CallRowMapper implements RowMapper<Call> {
    public Call mapRow(ResultSet rs, int arg1) throws SQLException {
        Date timeAndDate = rs.getDate("TIME_AND_DATE");
        String notes = rs.getString("NOTES");

        return new Call(notes, timeAndDate);
    }
}

