package com.exactpro.test.DAO;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.crutches.DBConnection;
import com.exactpro.entities.Customer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

class GenericDAOTest {

    GenericDAO genericDAO = new GenericDAO();
    Customer customer;

    @BeforeEach
    void setUp() {
        DBConnection.setConnectionData("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
        customer = new Customer("TEST", "UNIT", (short) 20, 0);
    }

    @AfterAll
    public void tearDown() throws SQLException, ClassNotFoundException {
        customer = null;
        DBConnection.executeNonResult("DELETE FROM CUSTOMERS");
        DBConnection.executeNonResult("ALTER TABLE CUSTOMERS AUTO_INCREMENT = 0");
        DBConnection.executeNonResult("DELETE FROM PRODUCTS");
        DBConnection.executeNonResult("ALTER TABLE PRODUCTS AUTO_INCREMENT = 0");
        DBConnection.executeNonResult("DELETE FROM DEALS");
        DBConnection.executeNonResult("ALTER TABLE DEALS AUTO_INCREMENT = 0");
    }

    @Test
    void insertEntity() {
        genericDAO.insertEntity(customer);
    }

    @Test
    void updateTableByEntity() throws SQLException, ClassNotFoundException {
        genericDAO.insertEntity(customer);
        customer.setName("UPDATED TEST");
        customer.setSurname("UPDATED UNIT");
        customer.setAge((short) 12);
        genericDAO.updateTableByEntity(customer);

        ResultSet resultSet = DBConnection.executeWithResult(
                String.format(
                        "SELECT customer_id, name, surname, age, favourite_product\n" +
                            "FROM hibernate_unittests.customers WHERE customer_id = %s"
                        , customer.getCustomerID()));
        resultSet.next();

        Assert.assertEquals(customer.getCustomerID(), resultSet.getInt("customer_id"));
        Assert.assertEquals(customer.getAge(), resultSet.getShort("age"));
        Assert.assertEquals(customer.getName(), resultSet.getString("name"));
        Assert.assertEquals(customer.getSurname(), resultSet.getString("surname"));
        Assert.assertEquals((int) customer.getFavouriteProduct(), resultSet.getInt("favourite_product"));

    }

    @Test
    void selectByID() {
        int selectingID = genericDAO.insertEntity(customer);
        Assert.assertEquals(customer, genericDAO.selectByID(Customer.class, selectingID));
    }

    @Test
    void gelAllEntities() throws SQLException, ClassNotFoundException {

        genericDAO.insertEntity(customer);
        genericDAO.insertEntity(customer);
        genericDAO.insertEntity(customer);

        int hibernateResultSize = genericDAO.gelAllEntities(Customer.class).size();

        ResultSet resultSet = DBConnection.executeWithResult("SELECT COUNT(*) AS count FROM customers");
        resultSet.next();
        long customResultSize = resultSet.getInt("count");

        Assert.assertEquals(hibernateResultSize, customResultSize);
    }

    @Test
    void deleteEntityFromTable() throws SQLException, ClassNotFoundException {
        int customerID = genericDAO.insertEntity(customer);
        genericDAO.deleteEntityFromTable(customer);

        ResultSet resultSet = DBConnection.executeWithResult(
                String.format("SELECT EXISTS(SELECT 1 FROM CUSTOMERS C WHERE CUSTOMER_ID = %s) AS exist", customerID));
        resultSet.next();
        Assert.assertFalse(resultSet.getBoolean("exist"));
    }
}