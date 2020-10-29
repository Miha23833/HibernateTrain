package com.exactpro.test.cache;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.cache.DealService;
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
import java.util.ArrayList;
import java.util.List;

class DealServiceTest {

    private Deal deal;
    private Product product;
    private Customer customer;
    private final SessionFactory sf = SingleSessionFactory.getInstance();
    DealService service = new DealService();

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
        session.getTransaction().commit();
        session.close();

    }

    @AfterEach
    void cleanTest() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
    }

    @Test
    void insertDeal() {

        List<Deal> deals = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Deal newDeal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50), new BigDecimal(0));
            DealService.insertDeal(newDeal);
            if (i > service.maxSize()-1){
                deals.add(newDeal);
            }
        }

        for (Deal value: deals) {
            Assert.assertEquals(value.hashCode(), DealService.getByID(value.getDealID()).hashCode());
            Assert.assertEquals(value, DealService.getByID(value.getDealID()));
        }

    }

    @Test
    void updateDeal() {
        DealService.insertDeal(deal);
        // currentTimeMillis будет иное
        Deal duplicateDeal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50), new BigDecimal(0));
        duplicateDeal.setDealID(deal.getDealID());
        duplicateDeal.setDealDate(System.currentTimeMillis());

        DealService.updateDeal(duplicateDeal);
        Deal updatedDeal = DealService.getByID(deal.getDealID());

        Assert.assertNotEquals(deal, updatedDeal);
        Assert.assertEquals(duplicateDeal, updatedDeal);

    }

    @Test
    void deleteDeal() {
        DealService.insertDeal(deal);
        Deal newDeal = DealService.getByID(deal.getDealID());

        Assert.assertEquals(deal, newDeal);

        DealService.deleteDeal(deal);

        Deal deletedDeal = DealService.getByID(deal.getDealID());

        Assert.assertNotEquals(newDeal, deletedDeal);
    }

    @Test
    void clean() {
    }
}