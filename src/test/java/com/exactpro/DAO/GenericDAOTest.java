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

//    @Before
//    public void setBefore(){
//        dao = new GenericDAO();
//        customer = new Customer("TestCustomer", "TestCustomerSurname", (short) 34, 0);
//    }

    @Test
    public void testAllGenericDAO() throws SQLException {
        int id = dao.insertEntity(customer);
        int id2 = customer.getCustomerID();
        customer.setCustomerID(id);

        short age = customer.getAge();
        customer.setAge((short) (age+1));
        dao.updateTableByEntity(customer);

        Assert.assertEquals(customer, dao.selectByID(Customer.class, customer.getCustomerID()));

        List<Customer> allCustomers = dao.gelAllEntities(Customer.class);
        Assert.assertNotEquals(allCustomers.size(), 0);

        dao.deleteEntityFromTable(customer);



    }

}