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
import com.opencsv.exceptions.CsvException;
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
import java.util.Map;

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
    void insertData() throws SQLException, ClassNotFoundException, IOException, CsvException {

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
        session.close();

        ResultSet csvData = DBConnection.executeWithResult("SELECT deal_date, customer_id, discount, product_id, price, deal_id FROM HIBERNATE_UNITTESTS.DEALS");

        String path = "testData";
        String filename = "testCSV";
        char separator = ';';

        Files.createDirectories(Paths.get(path));
        DataWorker worker = new DealDataWorker();

        filename = worker.saveDataToCSV(csvData, path, filename, separator).replace(".csv", "");

        Map<String, List<String>> dataFromCSVFile = worker.getDataFromCSVReader(path, filename);

        ResultSetMetaData csvMetaData = csvData.getMetaData();

        Assert.assertEquals(6, csvMetaData.getColumnCount());
        Assert.assertEquals(6, dataFromCSVFile.size());

        for (String key :dataFromCSVFile.keySet()){
            Assert.assertEquals(rowCount, dataFromCSVFile.get(key).size());
        }
        Assert.assertEquals(rowCount, getResultSetRowCount(csvData));

        int rowNumber = 0;
        while (csvData.next()) {
            Assert.assertEquals(csvData.getLong("deal_date"), Long.parseLong(dataFromCSVFile.get("deal_date").get(rowNumber)));
            Assert.assertEquals(csvData.getInt("customer_id"), Integer.parseInt(dataFromCSVFile.get("customer_id").get(rowNumber)));
            Assert.assertEquals(csvData.getInt("product_id"), Integer.parseInt(dataFromCSVFile.get("product_id").get(rowNumber)));
            Assert.assertEquals(csvData.getInt("deal_id"), Integer.parseInt(dataFromCSVFile.get("deal_id").get(rowNumber)));
            Assert.assertEquals(csvData.getBigDecimal("discount"), new BigDecimal(dataFromCSVFile.get("discount").get(rowNumber)));
            Assert.assertEquals(csvData.getBigDecimal("price"), new BigDecimal(dataFromCSVFile.get("price").get(rowNumber)));
            rowNumber++;
        }

    }
}