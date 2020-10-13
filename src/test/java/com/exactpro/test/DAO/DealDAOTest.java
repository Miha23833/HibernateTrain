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

        List<Deal> dealList = DealDAO.getByDate(compDate, ComparisonOperator.EQUAL);

        // равного по времени - 1, меньше и больше - по 5
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