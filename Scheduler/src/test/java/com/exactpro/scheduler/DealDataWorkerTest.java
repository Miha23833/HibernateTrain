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
import java.sql.ResultSetMetaData;
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

    private int getResultSetRowCount(ResultSet resultSet) throws SQLException {
        int result = 0;
        resultSet.beforeFirst();
        while (resultSet.next()){
            result++;
        }
        resultSet.beforeFirst();
        return result;
    }


    @Test
    void insertData() throws SQLException, ClassNotFoundException, IOException {

        ResultSet expectedZero = DBConnection.executeWithResult("SELECT COUNT(*) AS count FROM HIBERNATE_UNITTESTS.DEALS");
        expectedZero.next();

        int rowCount = 30;

        Assert.assertEquals(0, expectedZero.getInt("count"));

        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();

        for (int i = 0; i < rowCount; i++) {
            Deal fillDeal = new Deal(customer,product,deal.getDealDate(),deal.getPrice(),deal.getDiscount());

            GenericDAO.insertEntity(session, fillDeal);
        }
        session.getTransaction().commit();

        ResultSet csvData = DBConnection.executeWithResult("SELECT deal_date, customer_id, discount, product_id, price, deal_id FROM HIBERNATE_UNITTESTS.DEALS");

        String path = "testData";
        String filename = "testCSV";
        char separator = ';';

        Files.createDirectories(Paths.get(path));
        DataWorker worker = new DealDataWorker();

        filename = worker.saveDataToCSV(csvData, path, filename, separator);

        ResultSet dataFromCSVFile = worker.getDataFromCSV(path, filename, separator);

        ResultSetMetaData csvMetaData = csvData.getMetaData();
        ResultSetMetaData metaDataFromCSVFile = dataFromCSVFile.getMetaData();
        Assert.assertEquals(csvMetaData.getColumnCount(), 6);
        Assert.assertEquals(metaDataFromCSVFile.getColumnCount(), 6);

        Assert.assertEquals(rowCount, getResultSetRowCount(dataFromCSVFile));
        Assert.assertEquals(rowCount, getResultSetRowCount(csvData));

        while (csvData.next() && dataFromCSVFile.next()) {
            Assert.assertEquals(csvData.getLong("deal_date"), dataFromCSVFile.getLong("deal_date"));
            Assert.assertEquals(csvData.getInt("customer_id"), dataFromCSVFile.getInt("customer_id"));
            Assert.assertEquals(csvData.getInt("product_id"), dataFromCSVFile.getInt("product_id"));
            Assert.assertEquals(csvData.getInt("deal_id"), dataFromCSVFile.getInt("deal_id"));
            Assert.assertEquals(csvData.getBigDecimal("discount"), dataFromCSVFile.getBigDecimal("discount"));
            Assert.assertEquals(csvData.getBigDecimal("price"), dataFromCSVFile.getBigDecimal("price"));
        }

    }
}