package com.exactpro.scheduler.dataReader;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.StaticMethods;
import com.exactpro.scheduler.config.Config;
import com.exactpro.scheduler.dataExchanger.DealExchanger;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class ReaderThread implements Runnable {

    private final String[] dataColumns;
    private final String processFile;
    Logger warnLogger = StaticLogger.warnLogger;

    public ReaderThread(String filename, String[] dataColumns){
        this.processFile = filename;
        this.dataColumns = dataColumns;
    }

    private boolean validColumns(String[] csvFileColumns){
        if (csvFileColumns == null || csvFileColumns.length < dataColumns.length){
            return false;
        }
        return Arrays.asList(csvFileColumns).containsAll(Arrays.asList(dataColumns));
    }

    private Map<String, List<String>> csvToHashmap(List<String[]> csvData){
        Map<String, List<String>> result = new HashMap<>();

        String[] columnNames = csvData.get(0);

        for (String columnName: columnNames) {
            result.put(columnName, new ArrayList<>());
        }

        for (int dataRow = 1; dataRow < csvData.size(); dataRow++) {
            for (int dataColumn = 0; dataColumn < csvData.get(dataRow).length; dataColumn++) {
                result.get(columnNames[dataColumn]).add(csvData.get(dataRow)[dataColumn]);
            }
        }
        return result;
    }

    /**
     * Load data from csv to database if it is correct
     * and move file to "inserted" folder. If data is not
     * correct then move file to "rejected" folder.
     * Every file in process will be moved to dataInProgress folder
     * before reading.
     */
    @Override
    public void run() {
        try {
            StaticMethods.moveFile(Config.getFreshDataPath(), Config.getDataInProgressPath(), processFile, ".csv");

            CSVParser csvParser = new CSVParserBuilder()
                    .withSeparator(Config.getSeparator())
                    .withQuoteChar(Config.getQuoteChar()).build();

            try (CSVReader reader = new CSVReaderBuilder(new FileReader(Config.getDataInProgressPath() + processFile + ".csv")).withCSVParser(csvParser).build()) {

                String[] fileColumns = reader.readNext();

                if (!validColumns(fileColumns)) {
                    CsvValidationException exception = new CsvValidationException("File contains incorrect columns.");
                    warnLogger.error(exception);
                    throw exception;
                }

                List<String[]> resultCSV = new ArrayList<>();
                resultCSV.add(fileColumns);
                resultCSV.addAll(reader.readAll());

                Map<String, List<String>> resultMap = csvToHashmap(resultCSV);

                Session session = SingleSessionFactory.getInstance().openSession();
                try {

                    for (int i = 0; i < resultCSV.size(); i++) {
                        Deal dealToInsert = new Deal();

                        Product product = GenericDAO.selectByID(session, Product.class, Integer.parseInt(resultMap.get("product_id").get(i)));
                        Customer customer = GenericDAO.selectByID(session, Customer.class, Integer.parseInt(resultMap.get("customer_id").get(i)));

                        if (product == null && customer == null) {
                            throw new SQLException("Product and customer does not exist.");
                        }
                        else if (product == null) {
                            throw new SQLException("Product does not exist.");
                        }
                        else if (customer == null) {
                            throw new SQLException("Customer does not exist.");
                        }

                        dealToInsert.setDealDate(Long.parseLong(resultMap.get("deal_date").get(i)));
                        dealToInsert.setPrice(new BigDecimal(resultMap.get("price").get(i)));
                        dealToInsert.setDiscount(new BigDecimal(resultMap.get("discount").get(i)));
                        dealToInsert.setProduct(product);
                        dealToInsert.setCustomer(customer);

                        DealExchanger.put(dealToInsert);

                    }
                } catch (SQLException e){
                    warnLogger.error(e);
                }
                finally {
                    session.close();
                }

                // TODO: сделать лимитированным, так как при большом файле можно словить OutOfMemoryError
            }
        }catch (CsvException | IOException e) {
            warnLogger.error(e);
            try {
                StaticMethods.moveFile(Config.getDataInProgressPath(), Config.getRejectedDataPath(), processFile, ".csv");
            } catch (IOException ioException) {
                warnLogger.error(ioException);
            }
        }
    }

}
