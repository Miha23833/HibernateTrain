package com.exactpro.test.DAO;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.connection.DBConnection;
import com.exactpro.entities.Customer;
import com.exactpro.test.common.CommonUnitTests;
import org.hibernate.Session;
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
        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        GenericDAO.insertEntity(insertSession, customer);
        insertSession.getTransaction().commit();
        insertSession.close();
    }

    @Test
    void updateTableByEntity() throws SQLException, ClassNotFoundException {

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        GenericDAO.insertEntity(insertSession, customer);
        insertSession.getTransaction().commit();
        insertSession.close();

        customer.setName("UPDATED TEST");
        customer.setSurname("UPDATED UNIT");
        customer.setAge((short) 12);

        Session updateSession = sf.openSession();
        updateSession.beginTransaction();
        GenericDAO.updateTableByEntity(updateSession, customer);
        updateSession.getTransaction().commit();

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

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        int selectingID = GenericDAO.insertEntity(insertSession, customer);
        insertSession.getTransaction().commit();

        Session selectSession = sf.openSession();
        Customer newCustomer = GenericDAO.selectByID(selectSession, Customer.class, selectingID);
        selectSession.close();

        Assert.assertEquals(customer, newCustomer);
    }

    @Test
    void gelAllEntities() throws SQLException, ClassNotFoundException {

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        for (int i = 0; i < 3; i++) {
            Customer newCustomer = new Customer(customer.getName(), customer.getSurname(), customer.getAge(), customer.getFavouriteProduct());
            GenericDAO.insertEntity(insertSession, newCustomer);
        }
        insertSession.getTransaction().commit();
        insertSession.close();

        int hibernateResultSize = GenericDAO.gelAllEntities(sf.openSession(), Customer.class).size();

        ResultSet resultSet = DBConnection.executeWithResult("SELECT COUNT(*) AS count FROM customers");
        resultSet.next();
        long customResultSize = resultSet.getInt("count");

        Assert.assertEquals(hibernateResultSize, customResultSize);
    }

    @Test
    void deleteEntityFromTable() throws SQLException, ClassNotFoundException {

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        int customerID = GenericDAO.insertEntity(insertSession, customer);
        insertSession.getTransaction().commit();
        insertSession.close();

        Session deleteSession = sf.openSession();
        deleteSession.beginTransaction();
        GenericDAO.deleteEntityFromTable(deleteSession, customer);
        deleteSession.getTransaction().commit();
        deleteSession.close();


        ResultSet resultSet = DBConnection.executeWithResult(
                String.format("SELECT EXISTS(SELECT 1 FROM CUSTOMERS C WHERE CUSTOMER_ID = %s) AS exist", customerID));
        resultSet.next();
        Assert.assertFalse(resultSet.getBoolean("exist"));
    }
}