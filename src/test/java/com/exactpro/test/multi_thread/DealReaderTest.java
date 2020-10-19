package com.exactpro.test.multi_thread;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.cache.DealService;
import com.exactpro.datareader.DealReader;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.exactpro.test.common.CommonUnitTests;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

public class DealReaderTest {

    private final SessionFactory sf = SingleSessionFactory.getInstance();

    @Before
    public void launchAllTests() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
    }


    @BeforeEach
    void launchTest(){
    }

    @AfterEach
    void cleanTest() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
    }

    @Test
    public void dealReaderTest() throws InterruptedException {

        Deal[] deals = new Deal[100];
        DealReader[] dealReaders = new DealReader[deals.length];

        Product product = new Product("Name", "Some desc", new BigDecimal(50000));
        Customer customer = new Customer("TEST", "UNIT", (short) 20, null);

        Session session = sf.openSession();
        session.beginTransaction();

        GenericDAO.insertEntity(session, product);
        GenericDAO.insertEntity(session, customer);

        session.getTransaction().commit();
        session.close();

        for (int i = 0; i < deals.length; i++) {
            Deal newDeal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50), new BigDecimal(0));
            DealService.insertDeal(newDeal);
            deals[i] = newDeal;
        }

        for (int i = 0; i < dealReaders.length; i++) {
            dealReaders[i] = new DealReader(deals[i].getDealID());
            dealReaders[i].start();
        }

        for (int i = 0; i < deals.length; i++) {
            dealReaders[i].join();
            Assert.assertEquals(deals[i].getDealID(), dealReaders[i].getDeal().getDealID());
        }
    }

}
