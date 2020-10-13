package com.exactpro.test.DAO;

import com.exactpro.DAO.ComparisonOperator;
import com.exactpro.DAO.DealDAO;
import com.exactpro.DAO.GenericDAO;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.exactpro.test.common.CommonUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

class DealDAOTest {

    private Deal deal;
    private Product product;
    private Customer customer;

    @Before
    public void launchAllTests() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
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
    }

    @Test
    void getByDate() {
        Long compDate = deal.getDealDate();

        GenericDAO.insertEntity(customer);
        GenericDAO.insertEntity(product);
        GenericDAO.insertEntity(deal);

        for (int i = 1; i < 6; i++) {
            Deal newDeal = new Deal(customer, product, compDate-i*1000, new BigDecimal(50), new BigDecimal(0));
            GenericDAO.insertEntity(newDeal);
            newDeal = new Deal(customer, product, compDate+i*1000, new BigDecimal(50), new BigDecimal(0));
            GenericDAO.insertEntity(newDeal);
        }

        // Сравнение по количеству возвращённых записей для учитывания всех енумов
        Assert.assertEquals(1, DealDAO.getByDate(compDate, ComparisonOperator.EQUAL).size());

        Assert.assertEquals(10, DealDAO.getByDate(compDate, ComparisonOperator.NOT_EQUAL).size());

        Assert.assertEquals(5, DealDAO.getByDate(compDate, ComparisonOperator.GREATHER_THAN).size());

        Assert.assertEquals(6, DealDAO.getByDate(compDate, ComparisonOperator.GREATHER_THAN_OR_EQUAL).size());

        Assert.assertEquals(5, DealDAO.getByDate(compDate, ComparisonOperator.LESS_THAN).size());

        Assert.assertEquals(6, DealDAO.getByDate(compDate, ComparisonOperator.LESS_THAN_OR_EQUAL).size());
    }

    @Test
    void getByDiscount() {
        deal.setDiscount(new BigDecimal(50));
        BigDecimal compDiscount = deal.getDiscount();

        GenericDAO.insertEntity(customer);
        GenericDAO.insertEntity(product);
        GenericDAO.insertEntity(deal);

        for (int i = 1; i < 6; i++) {
            Deal newDeal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50), new BigDecimal(50+i));
            GenericDAO.insertEntity(newDeal);
            newDeal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50), new BigDecimal(50-i));
            GenericDAO.insertEntity(newDeal);
        }

        // Сравнение по количеству возвращённых записей для учитывания всех енумов
        Assert.assertEquals(1, DealDAO.getByDiscount(compDiscount, ComparisonOperator.EQUAL).size());

        Assert.assertEquals(10, DealDAO.getByDiscount(compDiscount, ComparisonOperator.NOT_EQUAL).size());

        Assert.assertEquals(5, DealDAO.getByDiscount(compDiscount, ComparisonOperator.GREATHER_THAN).size());

        Assert.assertEquals(6, DealDAO.getByDiscount(compDiscount, ComparisonOperator.GREATHER_THAN_OR_EQUAL).size());

        Assert.assertEquals(5, DealDAO.getByDiscount(compDiscount, ComparisonOperator.LESS_THAN).size());

        Assert.assertEquals(6, DealDAO.getByDiscount(compDiscount, ComparisonOperator.LESS_THAN_OR_EQUAL).size());
    }

    @Test
    void getByPrice() {
        deal.setPrice(new BigDecimal(50));
        BigDecimal compPrice = deal.getPrice();

        GenericDAO.insertEntity(customer);
        GenericDAO.insertEntity(product);
        GenericDAO.insertEntity(deal);

        for (int i = 1; i < 6; i++) {
            Deal newDeal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50+i), new BigDecimal(50));
            GenericDAO.insertEntity(newDeal);
            newDeal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50-i), new BigDecimal(50));
            GenericDAO.insertEntity(newDeal);
        }

        // Сравнение по количеству возвращённых записей для учитывания всех енумов
        Assert.assertEquals(1, DealDAO.getByPrice(compPrice, ComparisonOperator.EQUAL).size());

        Assert.assertEquals(10, DealDAO.getByPrice(compPrice, ComparisonOperator.NOT_EQUAL).size());

        Assert.assertEquals(5, DealDAO.getByPrice(compPrice, ComparisonOperator.GREATHER_THAN).size());

        Assert.assertEquals(6, DealDAO.getByPrice(compPrice, ComparisonOperator.GREATHER_THAN_OR_EQUAL).size());

        Assert.assertEquals(5, DealDAO.getByPrice(compPrice, ComparisonOperator.LESS_THAN).size());

        Assert.assertEquals(6, DealDAO.getByPrice(compPrice, ComparisonOperator.LESS_THAN_OR_EQUAL).size());
    }

    @Test
    void getByCustomerID() {
        int id = GenericDAO.insertEntity(customer);
        GenericDAO.insertEntity(product);
        GenericDAO.insertEntity(deal);

        for (int i = 0; i < 3; i++) {
            Customer otherCustomer = new Customer("UNIT", "TEST", (short) 10, null);
            GenericDAO.insertEntity(otherCustomer);
            for (int j = 0; j < 4; j++) {
                Deal otherDeal = new Deal(otherCustomer, product, System.currentTimeMillis(), product.getPrice(), new BigDecimal(0));
                GenericDAO.insertEntity(otherDeal);
            }
        }

        List<Deal> deals = DealDAO.getByCustomerID(id);

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
        GenericDAO.insertEntity(customer);
        int id = GenericDAO.insertEntity(product);
        GenericDAO.insertEntity(deal);

        for (int i = 0; i < 3; i++) {
            Product otherProduct = new Product("Чай беседа", "Лучший чай", new BigDecimal(i) );
            GenericDAO.insertEntity(otherProduct);
            for (int j = 0; j < 4; j++) {
                Deal otherDeal = new Deal(customer, otherProduct, System.currentTimeMillis(), product.getPrice(), new BigDecimal(0));
                GenericDAO.insertEntity(otherDeal);
            }
        }

        List<Deal> deals = DealDAO.getByProductID(id);

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

        GenericDAO.insertEntity(customer);
        GenericDAO.insertEntity(product);
        GenericDAO.insertEntity(deal);

        for (int i = 1; i < 6; i++) {
            Deal newDeal = new Deal(customer, product, compDate-i*1000, new BigDecimal(50), new BigDecimal(0));
            GenericDAO.insertEntity(newDeal);
            newDeal = new Deal(customer, product, compDate+i*1000, new BigDecimal(50), new BigDecimal(0));
            GenericDAO.insertEntity(newDeal);
        }

        List<Deal> deals = DealDAO.getByPeriod(compDate-3000,compDate+3000);

        for (Deal compDeal: deals) {
            Assert.assertTrue(compDeal.getDealDate() >= compDate-3000);
            Assert.assertTrue(compDeal.getDealDate() <= compDate+3000);
        }
    }
}