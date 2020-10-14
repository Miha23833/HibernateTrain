package com.exactpro.test.DAO;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.connection.DBConnection;
import com.exactpro.entities.Customer;
import com.exactpro.test.common.CommonUnitTests;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

class GenericDAOTest {

    Customer customer;
    private final SessionFactory sf = SingleSessionFactory.getInstance();

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
        customer = new Customer("TEST", "UNIT", (short) 20, 0);
    }

    @After
    public void tearDown() throws SQLException, ClassNotFoundException {
        customer = null;
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
    }

    @Test
    void insertEntity() {
        GenericDAO.insertEntity(sf.openSession(), customer);
    }

    @Test
    void updateTableByEntity() throws SQLException, ClassNotFoundException {
        GenericDAO.insertEntity(sf.openSession(), customer);
        customer.setName("UPDATED TEST");
        customer.setSurname("UPDATED UNIT");
        customer.setAge((short) 12);
        GenericDAO.updateTableByEntity(sf.openSession(), customer);

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
        int selectingID = GenericDAO.insertEntity(sf.openSession(), customer);
        Customer newCustomer = GenericDAO.selectByID(sf.openSession(), Customer.class, selectingID);
        Assert.assertEquals(customer, newCustomer);
    }

    @Test
    void gelAllEntities() throws SQLException, ClassNotFoundException {

        GenericDAO.insertEntity(sf.openSession(), customer);
        GenericDAO.insertEntity(sf.openSession(), customer);
        GenericDAO.insertEntity(sf.openSession(), customer);

        int hibernateResultSize = GenericDAO.gelAllEntities(sf.openSession(), Customer.class).size();

        ResultSet resultSet = DBConnection.executeWithResult("SELECT COUNT(*) AS count FROM customers");
        resultSet.next();
        long customResultSize = resultSet.getInt("count");

        Assert.assertEquals(hibernateResultSize, customResultSize);
    }

    @Test
    void deleteEntityFromTable() throws SQLException, ClassNotFoundException {
        int customerID = GenericDAO.insertEntity(sf.openSession(), customer);
        GenericDAO.deleteEntityFromTable(sf.openSession(), customer);

        ResultSet resultSet = DBConnection.executeWithResult(
                String.format("SELECT EXISTS(SELECT 1 FROM CUSTOMERS C WHERE CUSTOMER_ID = %s) AS exist", customerID));
        resultSet.next();
        Assert.assertFalse(resultSet.getBoolean("exist"));
    }
}