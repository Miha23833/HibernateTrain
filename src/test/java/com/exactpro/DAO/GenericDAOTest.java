package com.exactpro.DAO;

import com.exactpro.entities.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class GenericDAOTest {

    GenericDAO dao = new GenericDAO();;
    Customer customer = new Customer("TestCustomer", "TestCustomerSurname", (short) 34, 0);

    @Before
    public void setBefore(){
        dao = new GenericDAO();
        customer = new Customer("TestCustomer", "TestCustomerSurname", (short) 34, 0);
    }


}