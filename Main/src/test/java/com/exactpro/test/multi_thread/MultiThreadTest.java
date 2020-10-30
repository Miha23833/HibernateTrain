package com.exactpro.test.multi_thread;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.cache.DealService;
import com.exactpro.connection.DBConnection;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.multithread.DealReader;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.exactpro.multithread.DealWriter;
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
import java.sql.ResultSet;
import java.sql.SQLException;

public class MultiThreadTest {

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
    }

    @AfterEach
    void cleanTest() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");

        DealService service = new DealService();
        service.clean();

        infoLogger.info("Cache was cleaned during clearing database");
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
            // TODO: если сделать join в следующем цикле - падает. Подозреваю ленивую инициализацию
            dealReaders[i].join();
        }

        for (int i = 0; i < deals.length; i++) {
            Assert.assertEquals(deals[i].getDealID(), dealReaders[i].getDeal().getDealID());
        }
    }

    @Test
    public void dealWriterTest() throws InterruptedException, SQLException, ClassNotFoundException {
        Product product = new Product("Name", "Some desc", new BigDecimal(50000));
        Customer customer = new Customer("TEST", "UNIT", (short) 20, null);

        Session session = sf.openSession();
        session.beginTransaction();

        GenericDAO.insertEntity(session, product);
        GenericDAO.insertEntity(session, customer);

        session.getTransaction().commit();
        session.close();

        Deal[] deals = new Deal[300];
        DealWriter[] dealWriters = new DealWriter[deals.length];

        for (int i = 0; i < deals.length; i++) {
            Deal newDeal = new Deal(customer, product, System.currentTimeMillis(), new BigDecimal(50), new BigDecimal(0));
            deals[i] = newDeal;
            DealWriter writer = new DealWriter(newDeal);
            writer.start();
            dealWriters[i] = writer;
        }

        for (DealWriter dealWriter: dealWriters) {
            dealWriter.join();
        }

        DBConnection.setConnectionData("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
        ResultSet queryResult = DBConnection.executeWithResult("SELECT COUNT(*) AS count FROM DEALS");

        queryResult.next();
        Assert.assertEquals(deals.length, queryResult.getInt("count"));

        for (Deal deal: deals) {
            Deal compDeal = DealService.getByID(deal.getDealID());
            Assert.assertEquals(deal.getDealID(), compDeal.getDealID());
            Assert.assertEquals(deal.getDealDate(), compDeal.getDealDate());
            Assert.assertEquals(0, deal.getPrice().compareTo(compDeal.getPrice()));
            Assert.assertEquals(0, deal.getDiscount().compareTo(compDeal.getDiscount()));
        }
    }

}
