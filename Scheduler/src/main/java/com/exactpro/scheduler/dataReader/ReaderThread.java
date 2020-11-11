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
import com.opencsv.exceptions.CsvValidationException;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;


//TODO: переделать так, чтобы из файла бралось по одной строке и она уже должна быть в виде DEAL и попадать в DealExchanger
// вообще, можно сделать так, чтобы мы брали из первой строки имена столбцов и соотношали их с индексами в Map<String, Integer>
// и мы будем в пределах одного файла знать на каком месте какой столбец и, читая линию, обращаться к колонкам по имени, а не
// по индексу. В идеале - стоит вынести это в отдельную сущность. Подозреваю, что это можно сделать и в Generic-виде, но не факт.
// Раз уж я часть итак делаю как хардкод для Deal - пусть так и будет, если не получится.

//TODO: Подумать как можно реализовать оба эти класса как Generic, ибо разве мы хотим останавливаться на загрузке только одной сущности?
public class ReaderThread implements Runnable {

    private final String[] dataColumns;
    private final String processFile;
    Logger warnLogger = StaticLogger.warnLogger;

    public ReaderThread(String filename, String[] dataColumns) {
        this.processFile = filename;
        this.dataColumns = dataColumns;
    }

    private boolean validColumns(String[] csvFileColumns) {
        if (csvFileColumns == null || csvFileColumns.length < dataColumns.length) {
            return false;
        }
        return Arrays.asList(csvFileColumns).containsAll(Arrays.asList(dataColumns));
    }

    private Map<String, List<String>> csvToHashmap(List<String[]> csvData) {
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

    private List<Map<String, String>> csvToRecordSet(List<String[]> csvData, String[] columns) {
        List<Map<String, String>> result = new ArrayList<>();

        for (String[] row: csvData) {
            result.add(csvRowToMap(row, columns));
        }

        return result;
    }

    private Map<String, String> csvRowToMap(String[] csvRow, String[] columns) {
        if (csvRow.length != columns.length) {
            throw new IllegalArgumentException("Number of columns both in csvRow and columns must be equal!");
        }
        Map<String, String> record = new HashMap<>();
        for (int i = 0; i < csvRow.length; i++) {
            record.put(columns[i], csvRow[i]);
        }
        return record;
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

                try (Session session = SingleSessionFactory.getInstance().openSession()) {
                    while (reader.readNext() != null) {
                        Map<String, String> csvRow = csvRowToMap(reader.peek(), fileColumns);

                        Deal dealToInsert = new Deal();

                        Product product = GenericDAO.selectByID(session, Product.class, Integer.parseInt(csvRow.get("product_id")));
                        Customer customer = GenericDAO.selectByID(session, Customer.class, Integer.parseInt(csvRow.get("customer_id")));

                        if (product == null && customer == null) {
                            throw new SQLException("Product and customer does not exist.");
                        }
                        else if (product == null) {
                            throw new SQLException("Product does not exist.");
                        }
                        else if (customer == null) {
                            throw new SQLException("Customer does not exist.");
                        }

                        dealToInsert.setDealDate(Long.parseLong(csvRow.get("deal_date")));
                        dealToInsert.setPrice(new BigDecimal(csvRow.get("price")));
                        dealToInsert.setDiscount(new BigDecimal(csvRow.get("discount")));
                        dealToInsert.setProduct(product);
                        dealToInsert.setCustomer(customer);

                        DealExchanger.put(dealToInsert);
                    }
                }
            }
        } catch (Exception e) {
            warnLogger.error(e);
            try {
                StaticMethods.moveFile(Config.getDataInProgressPath(), Config.getRejectedDataPath(), processFile, ".csv");
            } catch (IOException ioException) {
                warnLogger.error(ioException);
            }
        }
    }

}
