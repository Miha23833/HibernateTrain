package com.exactpro.scheduler;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.cache.DealService;
import com.exactpro.connection.DBConnection;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.tests.CommonUnitTests;
import com.mysql.cj.jdbc.result.ResultSetImpl;
import com.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DealDataWorkerTest {

    private Deal deal;
    private Product product;
    private Customer customer;
    private final SessionFactory sf = SingleSessionFactory.getInstance();

    Logger infoLogger = StaticLogger.infoLogger;

    @Before
    public void launchAllTests() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");

        DealService service = new DealService();
        service.clean();
        infoLogger.info("Cache was cleaned during clearing database");
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

        DealService service = new DealService();
        service.clean();

        infoLogger.info("Cache was cleaned during clearing database");
    }


    @Test
    void insertData() throws SQLException, ClassNotFoundException, IOException {

        ResultSet rowCount = DBConnection.executeWithResult("SELECT COUNT(*) AS count FROM HIBERNATE_UNITTESTS.DEALS");
        rowCount.next();

        Assert.assertEquals(0, rowCount.getInt("count"));

        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();

        for (int i = 0; i < 30; i++) {
            Deal fillDeal = new Deal(customer,product,deal.getDealDate(),deal.getPrice(),deal.getDiscount());

            GenericDAO.insertEntity(session, fillDeal);
        }
        session.getTransaction().commit();

        ResultSet csvData = DBConnection.executeWithResult("SELECT * FROM HIBERNATE_UNITTESTS.DEALS");

        String path = "testData";
        String filename = "testCSV";
        char separator = ';';

        Files.createDirectories(Paths.get(path));
        DataWorker worker = new DealDataWorker();

        filename = worker.saveDataToCSV(csvData, path, filename, separator);

        ResultSet dataFromCSVFile = worker.getDataFromCSV(path, filename, separator);

//        Assert.assertTrue(csvData.next() && dataFromCSVFile.next());
        csvData.first();
        dataFromCSVFile.first();

        int col = 1;
        while (csvData.next() && dataFromCSVFile.next()) {
            final Object res1 = csvData.getObject(col);
            final Object res2 = dataFromCSVFile.getObject(col);
            // Check values
            Assert.assertEquals(res1, res2);

            // csvData and dataFromCSVFile must reach last row in the same iteration
            Assert.assertEquals(csvData.isLast(), dataFromCSVFile.isLast());

            col++;
        }



    }
}