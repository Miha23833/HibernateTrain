package com.exactpro.scheduler.dataWriter;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.StaticMethods;
import com.exactpro.scheduler.config.Config;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class WriterThread implements Runnable {

    private static final Logger warnLogger = StaticLogger.warnLogger;
    private final Map<String, List<String>> data;
    private final Integer rowCount;
    private final String filename;


    public WriterThread(Map<String, List<String>> data, String filename) throws IOException {
        if (!Collections.singletonList(data.keySet()).containsAll(Arrays.asList(Config.getCSVColumns()))) {
            throw new RuntimeException("Read csv data does not contain all columns from config");
        }

        Integer rowCount = null;
        for (String key: data.keySet()) {
            if (rowCount == null) {
                rowCount = data.get(key).size();
                continue;
            }

            if (rowCount != data.get(key).size() || data.get(key).size() == 0) {
                StaticMethods.moveFile(Config.getDataInProgressPath(), Config.getRejectedDataPath(), filename, ".csv");
                throw new RuntimeException(String.format("Rows count are not equal or one of row count is 0. Filename: %s, row: %s, count: %s", filename, key, data.get(key).size()));
            }

        }
        this.rowCount = rowCount;
        this.filename = filename;

        this.data = data;
    }


    /**
     * Insert map-view data table to database.
     */
    @Override
    public void run() {
        Session session = SingleSessionFactory.getInstance().openSession();

        try {
            for (int i = 0; i < rowCount; i++) {
                Deal dealToInsert = new Deal();

                Product product = GenericDAO.selectByID(session, Product.class, Integer.parseInt(data.get("product_id").get(i)));
                Customer customer = GenericDAO.selectByID(session, Customer.class, Integer.parseInt(data.get("customer_id").get(i)));

                if (product == null && customer == null) {
                    throw new SQLException("Product and customer does not exist.");
                }
                else if (product == null) {
                    throw new SQLException("Product does not exist.");
                }
                else if (customer == null) {
                    throw new SQLException("Customer does not exist.");
                }

                dealToInsert.setDealDate(Long.parseLong(data.get("deal_date").get(i)));
                dealToInsert.setPrice(new BigDecimal(data.get("price").get(i)));
                dealToInsert.setDiscount(new BigDecimal(data.get("discount").get(i)));
                dealToInsert.setProduct(product);
                dealToInsert.setCustomer(customer);

                GenericDAO.insertEntity(session, dealToInsert);

            }
            StaticMethods.moveFile(Config.getDataInProgressPath(), Config.getInsertedDataPath(), filename, ".csv");

        } catch (Exception e) {
            warnLogger.error(e);
            try {
                StaticMethods.moveFile(Config.getDataInProgressPath(), Config.getRejectedDataPath(), filename, ".csv");
            } catch (IOException moveFileException) {
                warnLogger.error(String.format
                                ("Cannot move file %s%s from %s to %s", filename, ".csv", Config.getDataInProgressPath(), Config.getRejectedDataPath())
                        , moveFileException);
            }
        }
    }
}
