package com.exactpro.test.DAO;

import com.exactpro.DAO.ComparisonOperator;
import com.exactpro.DAO.DealDAO;
import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.cache.DealService;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.tests.CommonUnitTests;
import org.apache.log4j.Logger;
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

class DealDAOTest {

    private Deal deal;
    private Product product;
    private Customer customer;
    private final SessionFactory sf = SingleSessionFactory.getInstance();

    private final Logger infoLogger = StaticLogger.infoLogger;

    @Before
    public void launchAllTests() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");

        DealService service = new DealService();
        service.clean();
        infoLogger.info("Cache was cleaned during clearing database");
    }


    @BeforeEach
    void launchTest(){
        product = new Product("Name", "Some desc", new BigDecimal(50000));
        customer = new Customer("TEST", "UNIT", (short) 20, null);
        deal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50), new BigDecimal(0));
    }

    @AfterEach
    void cleanTest() throws SQLException, ClassNotFoundException {
        product = null;
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");

        DealService service = new DealService();
        service.clean();

        infoLogger.info("Cache was cleaned during clearing database");
    }

    @Test
    void getByDate() {
        Long compDate = deal.getDealDate();

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        GenericDAO.insertEntity(insertSession, customer);
        GenericDAO.insertEntity(insertSession, product);
        GenericDAO.insertEntity(insertSession, deal);

        for (int i = 1; i < 6; i++) {
            Deal newDeal = new Deal(customer, product, compDate-i*1000, new BigDecimal(50), new BigDecimal(0));
            GenericDAO.insertEntity(insertSession, newDeal);
            newDeal = new Deal(customer, product, compDate+i*1000, new BigDecimal(50), new BigDecimal(0));
            GenericDAO.insertEntity(insertSession, newDeal);
        }
        insertSession.getTransaction().commit();

        // Сравнение по количеству возвращённых записей для учитывания всех енумов
        Assert.assertEquals(1, DealDAO.getByDate(sf.openSession(), compDate, ComparisonOperator.EQUAL).size());

        Assert.assertEquals(10, DealDAO.getByDate(sf.openSession(), compDate, ComparisonOperator.NOT_EQUAL).size());

        Assert.assertEquals(5, DealDAO.getByDate(sf.openSession(), compDate, ComparisonOperator.GREATER_THAN).size());

        Assert.assertEquals(6, DealDAO.getByDate(sf.openSession(), compDate, ComparisonOperator.GREATER_THAN_OR_EQUAL).size());

        Assert.assertEquals(5, DealDAO.getByDate(sf.openSession(), compDate, ComparisonOperator.LESS_THAN).size());

        Assert.assertEquals(6, DealDAO.getByDate(sf.openSession(), compDate, ComparisonOperator.LESS_THAN_OR_EQUAL).size());
    }

    @Test
    void getByDiscount() {
        deal.setDiscount(new BigDecimal(50));
        BigDecimal compDiscount = deal.getDiscount();

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        GenericDAO.insertEntity(insertSession, customer);
        GenericDAO.insertEntity(insertSession, product);
        GenericDAO.insertEntity(insertSession, deal);

        for (int i = 1; i < 6; i++) {
            Deal newDeal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50), new BigDecimal(50+i));
            GenericDAO.insertEntity(insertSession, newDeal);
            newDeal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50), new BigDecimal(50-i));
            GenericDAO.insertEntity(insertSession, newDeal);
        }
        insertSession.getTransaction().commit();
        // Сравнение по количеству возвращённых записей для учитывания всех енумов
        Assert.assertEquals(1, DealDAO.getByDiscount(sf.openSession(), compDiscount, ComparisonOperator.EQUAL).size());

        Assert.assertEquals(10, DealDAO.getByDiscount(sf.openSession(), compDiscount, ComparisonOperator.NOT_EQUAL).size());

        Assert.assertEquals(5, DealDAO.getByDiscount(sf.openSession(), compDiscount, ComparisonOperator.GREATER_THAN).size());

        Assert.assertEquals(6, DealDAO.getByDiscount(sf.openSession(), compDiscount, ComparisonOperator.GREATER_THAN_OR_EQUAL).size());

        Assert.assertEquals(5, DealDAO.getByDiscount(sf.openSession(), compDiscount, ComparisonOperator.LESS_THAN).size());

        Assert.assertEquals(6, DealDAO.getByDiscount(sf.openSession(), compDiscount, ComparisonOperator.LESS_THAN_OR_EQUAL).size());
    }

    @Test
    void getByPrice() {
        deal.setPrice(new BigDecimal(50));
        BigDecimal compPrice = deal.getPrice();

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        GenericDAO.insertEntity(insertSession, customer);
        GenericDAO.insertEntity(insertSession, product);
        GenericDAO.insertEntity(insertSession, deal);

        for (int i = 1; i < 6; i++) {
            Deal newDeal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50+i), new BigDecimal(50));
            GenericDAO.insertEntity(insertSession, newDeal);
            newDeal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50-i), new BigDecimal(50));
            GenericDAO.insertEntity(insertSession, newDeal);
        }
        insertSession.getTransaction().commit();

        // Сравнение по количеству возвращённых записей для учитывания всех енумов
        Assert.assertEquals(1, DealDAO.getByPrice(sf.openSession(), compPrice, ComparisonOperator.EQUAL).size());

        Assert.assertEquals(10, DealDAO.getByPrice(sf.openSession(), compPrice, ComparisonOperator.NOT_EQUAL).size());

        Assert.assertEquals(5, DealDAO.getByPrice(sf.openSession(), compPrice, ComparisonOperator.GREATER_THAN).size());

        Assert.assertEquals(6, DealDAO.getByPrice(sf.openSession(), compPrice, ComparisonOperator.GREATER_THAN_OR_EQUAL).size());

        Assert.assertEquals(5, DealDAO.getByPrice(sf.openSession(), compPrice, ComparisonOperator.LESS_THAN).size());

        Assert.assertEquals(6, DealDAO.getByPrice(sf.openSession(), compPrice, ComparisonOperator.LESS_THAN_OR_EQUAL).size());
    }

    @Test
    void getByCustomerID() {

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();

        int id = GenericDAO.insertEntity(insertSession, customer);
        GenericDAO.insertEntity(insertSession, product);
        GenericDAO.insertEntity(insertSession, deal);

        for (int i = 0; i < 3; i++) {
            Customer otherCustomer = new Customer("UNIT", "TEST", (short) 10, null);
            GenericDAO.insertEntity(insertSession, otherCustomer);
            for (int j = 0; j < 4; j++) {
                Deal otherDeal = new Deal(otherCustomer, product, System.currentTimeMillis(), product.getPrice(), new BigDecimal(0));
                GenericDAO.insertEntity(insertSession, otherDeal);
            }
        }
        insertSession.getTransaction().commit();

        List<Deal> deals = DealDAO.getByCustomerID(sf.openSession(), id);

        Assert.assertEquals(1, deals.size());

        for (Deal compDeal: deals) {
            Assert.assertEquals(deal.getCustomer().getCustomerID(), compDeal.getCustomer().getCustomerID());
            Assert.assertEquals(deal.getProduct().getProductID(), compDeal.getProduct().getProductID());
            Assert.assertEquals(deal.getDealDate(), compDeal.getDealDate());
            Assert.assertEquals(0, deal.getDiscount().compareTo(compDeal.getDiscount()));
            Assert.assertEquals(deal.getDealID(), compDeal.getDealID());
            Assert.assertEquals(0, deal.getPrice().compareTo(compDeal.getPrice()));
        }

    }

    @Test
    void getByProductID() {

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        GenericDAO.insertEntity(insertSession, customer);
        int id = GenericDAO.insertEntity(insertSession, product);
        GenericDAO.insertEntity(insertSession, deal);

        for (int i = 0; i < 3; i++) {
            Product otherProduct = new Product("Чай беседа", "Лучший чай", new BigDecimal(i) );
            GenericDAO.insertEntity(insertSession, otherProduct);
            for (int j = 0; j < 4; j++) {
                Deal otherDeal = new Deal(customer, otherProduct, System.currentTimeMillis(), product.getPrice(), new BigDecimal(0));
                GenericDAO.insertEntity(insertSession, otherDeal);
            }
        }
        insertSession.getTransaction().commit();

        List<Deal> deals = DealDAO.getByProductID(sf.openSession(), id);

        Assert.assertEquals(1, deals.size());

        for (Deal compDeal: deals) {
            Assert.assertEquals(deal.getCustomer().getCustomerID(), compDeal.getCustomer().getCustomerID());
            Assert.assertEquals(deal.getProduct().getProductID(), compDeal.getProduct().getProductID());
            Assert.assertEquals(deal.getDealDate(), compDeal.getDealDate());
            Assert.assertEquals(0, deal.getDiscount().compareTo(compDeal.getDiscount()));
            Assert.assertEquals(deal.getDealID(), compDeal.getDealID());
            Assert.assertEquals(0, deal.getPrice().compareTo(compDeal.getPrice()));
        }

    }

    @Test
    void getByPeriod() {
        Long compDate = deal.getDealDate();

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        GenericDAO.insertEntity(insertSession, customer);
        GenericDAO.insertEntity(insertSession, product);
        GenericDAO.insertEntity(insertSession, deal);

        for (int i = 1; i < 6; i++) {
            Deal newDeal = new Deal(customer, product, compDate-i*1000, new BigDecimal(50), new BigDecimal(0));
            GenericDAO.insertEntity(insertSession, newDeal);
            newDeal = new Deal(customer, product, compDate+i*1000, new BigDecimal(50), new BigDecimal(0));
            GenericDAO.insertEntity(insertSession, newDeal);
        }
        insertSession.getTransaction().commit();

        List<Deal> deals = DealDAO.getByPeriod(sf.openSession(), compDate-3000,compDate+3000);

        Assert.assertEquals(7, deals.size());

        for (Deal compDeal: deals) {
            Assert.assertTrue(compDeal.getDealDate() >= compDate-3000);
            Assert.assertTrue(compDeal.getDealDate() <= compDate+3000);
        }
    }
}