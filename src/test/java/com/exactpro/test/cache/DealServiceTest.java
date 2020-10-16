package com.exactpro.test.cache;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.exactpro.test.common.CommonUnitTests;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

class DealServiceTest {

    private Deal deal;
    private Product product;
    private Customer customer;
    private final SessionFactory sf = SingleSessionFactory.getInstance();

    @Before
    public void launchAllTests() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
    }


    @BeforeEach
    void launchTest(){

        // Опорные значения
        product = new Product("Name", "Some desc", new BigDecimal(50000));
        customer = new Customer("TEST", "UNIT", (short) 20, null);
        deal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50), new BigDecimal(0));

        // Заполняем БД
        Session session = sf.openSession();
        session.beginTransaction();
        GenericDAO.insertEntity(session, product);
        GenericDAO.insertEntity(session, customer);
        GenericDAO.insertEntity(session, deal);
        session.getTransaction().commit();
        session.close();
    }

    @AfterEach
    void cleanTest() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
    }

    @Test
    void insertDeal() {
    }

    @Test
    void getByID() {
    }

    @Test
    void updateDeal() {
    }

    @Test
    void deleteDeal() {
    }

    @Test
    void clean() {
    }
}