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

        // Заполняем БД
        Session session = sf.openSession();
        session.beginTransaction();
        for (int i = 0; i < 20; i++) {
            Product newProduct = new Product("Name", "Some desc", new BigDecimal(50000));
            Customer newCustomer = new Customer("TEST", "UNIT", (short) 20, null);
            Deal newDeal = new Deal(newCustomer, newProduct, deal.getDealDate(), new BigDecimal(50), new BigDecimal(0));

            GenericDAO.insertEntity(session, newProduct);
            GenericDAO.insertEntity(session, newCustomer);
            GenericDAO.insertEntity(session, newDeal);
        }
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
    void getByDate() {
        List<Deal> dealsFromDB = DealCache.getByDate(deal.getDealDate(), ComparisonOperator.EQUAL);
        List<Deal> dealsFromCache = DealCache.getByDate(deal.getDealDate(), ComparisonOperator.EQUAL);

        Assert.assertTrue(dealsFromDB.size() > 0);

        Assert.assertEquals(dealsFromCache.hashCode(), dealsFromDB.hashCode());

        List<Deal> olderDealsFromDB = DealCache.getByDate(deal.getDealDate()+1, ComparisonOperator.LESS_THAN);
        List<Deal> olderDealsFromCache = DealCache.getByDate(deal.getDealDate()+1, ComparisonOperator.LESS_THAN);

        Assert.assertTrue(olderDealsFromDB.size() > 0);
        Assert.assertEquals(olderDealsFromDB.hashCode(), olderDealsFromCache.hashCode());
    }

    @Test
    void getByDiscount() {
        List<Deal> dealsFromDB = DealCache.getByDiscount(deal.getDiscount(), ComparisonOperator.EQUAL);
        List<Deal> dealsFromCache = DealCache.getByDiscount(deal.getDiscount(), ComparisonOperator.EQUAL);

        Assert.assertTrue(dealsFromDB.size() > 0);

        Assert.assertEquals(dealsFromCache.hashCode(), dealsFromDB.hashCode());

        List<Deal> lessDealsFromDB = DealCache.getByDiscount(deal.getDiscount().add(new BigDecimal(1)), ComparisonOperator.LESS_THAN);
        List<Deal> lessDealsFromCache = DealCache.getByDiscount(deal.getDiscount().add(new BigDecimal(1)), ComparisonOperator.LESS_THAN);

        Assert.assertTrue(lessDealsFromDB.size() > 0);
        Assert.assertEquals(lessDealsFromDB.hashCode(), lessDealsFromCache.hashCode());
    }

    @Test
    void getByPrice() {
        List<Deal> dealsFromDB = DealCache.getByPrice(deal.getPrice(), ComparisonOperator.EQUAL);
        List<Deal> dealsFromCache = DealCache.getByPrice(deal.getPrice(), ComparisonOperator.EQUAL);

        Assert.assertTrue(dealsFromDB.size() > 0);

        Assert.assertEquals(dealsFromCache.hashCode(), dealsFromDB.hashCode());

        List<Deal> lessDealsFromDB = DealCache.getByPrice(deal.getPrice().add(new BigDecimal(1)), ComparisonOperator.LESS_THAN);
        List<Deal> lessDealsFromCache = DealCache.getByPrice(deal.getPrice().add(new BigDecimal(1)), ComparisonOperator.LESS_THAN);

        Assert.assertTrue(lessDealsFromDB.size() > 0);
        Assert.assertEquals(lessDealsFromDB.hashCode(), lessDealsFromCache.hashCode());
    }

    @Test
    void getByCustomerID() {
        List<Deal> dealsFromDB = DealCache.getByCustomerID(deal.getCustomer().getCustomerID());
        List<Deal> dealsFromCache = DealCache.getByCustomerID(deal.getCustomer().getCustomerID());

        Assert.assertTrue(dealsFromDB.size() > 0);

        Assert.assertEquals(dealsFromCache.hashCode(), dealsFromDB.hashCode());
    }

    @Test
    void getByProductID() {
        List<Deal> dealsFromDB = DealCache.getByProductID(deal.getProduct().getProductID());
        List<Deal> dealsFromCache = DealCache.getByProductID(deal.getProduct().getProductID());

        Assert.assertTrue(dealsFromDB.size() > 0);

        Assert.assertEquals(dealsFromCache.hashCode(), dealsFromDB.hashCode());
    }

    @Test
    void getByPeriod() {
        List<Deal> dealsFromDB = DealCache.getByPeriod(deal.getDealDate()-1, deal.getDealDate()+1);
        List<Deal> dealsFromCache = DealCache.getByPeriod(deal.getDealDate()-1, deal.getDealDate()+1);

        Assert.assertTrue(dealsFromDB.size() > 0);

        Assert.assertEquals(dealsFromCache.hashCode(), dealsFromDB.hashCode());
    }
}