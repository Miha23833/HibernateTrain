package com.exactpro.scheduler.dataReader;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.StaticMethods;
import com.exactpro.scheduler.config.Config;
import com.exactpro.scheduler.dataExchanger.FileDataKeeper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
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

                FileDataKeeper.add(processFile, csvToHashmap(resultCSV));
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
