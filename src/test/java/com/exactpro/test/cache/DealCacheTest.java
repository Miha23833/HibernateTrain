package com.exactpro.test.cache;

import com.exactpro.DAO.ComparisonOperator;
import com.exactpro.DAO.DealDAO;
import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.cache.DealCache;
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
import java.util.List;

class DealCacheTest {

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

        Session session = sf.openSession();
        session.beginTransaction();
        for (int i = 0; i < 20; i++) {
            Product newProduct = new Product("Name", "Some desc", new BigDecimal(50000));
            Customer newCustomer = new Customer("TEST", "UNIT", (short) 20, null);
            Deal newDeal = new Deal(customer, newProduct, System.currentTimeMillis(), new BigDecimal(50), new BigDecimal(0));

            GenericDAO.insertEntity(session, newProduct);
            GenericDAO.insertEntity(session, newCustomer);
            GenericDAO.insertEntity(session, newDeal);
        }
        session.getTransaction().commit();
        session.close();
    }

    @AfterEach
    void cleanTest() throws SQLException, ClassNotFoundException {
        product = null;
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
    }


    @Test
    void getByDate() {

        List<Deal> dealsFromDB = DealCache.getByDate(deal.getDealDate(), ComparisonOperator.EQUAL);
        List<Deal> dealsFromCache = DealCache.getByDate(deal.getDealDate(), ComparisonOperator.EQUAL);

        Assert.assertTrue(dealsFromDB.size() > 0);

        Assert.assertEquals(dealsFromCache.hashCode(), dealsFromDB.hashCode());
    }

    @Test
    void getByDiscount() {
    }

    @Test
    void getByPrice() {
    }

    @Test
    void getByCustomerID() {
    }

    @Test
    void getByProductID() {
    }

    @Test
    void getByPeriod() {
    }
}