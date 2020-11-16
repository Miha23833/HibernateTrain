package com.exactpro.scheduler.dataReader;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.CSVMetaData;
import com.exactpro.scheduler.common.Record;
import com.exactpro.scheduler.common.StaticMethods;
import com.exactpro.scheduler.config.Config;
import com.exactpro.scheduler.dataExchanger.DataExchanger;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class ReaderThread implements Runnable {

    private String filename;
    Logger warnLogger = StaticLogger.warnLogger;

    public ReaderThread(String filename) {
        if (filename.contains(".")) {
            this.filename = filename.substring(0, filename.lastIndexOf("."));
        }
        else {
            this.filename = filename;
        }
    }

    private void skipMetaRows(CSVReader reader) throws IOException, CsvValidationException {
        while (reader.peek() != null && CSVMetaData.isMetaRow(reader.peek())) {
            reader.readNext();
        }
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

        StaticMethods.createFolders(new String[]{
                Config.getFreshDataPath(),
                Config.getDataInProgressPath(),
                Config.getViewedDataPath(),
                Config.getRootPath()
        });

        filename = StaticMethods.safeMoveFile(Config.getFreshDataPath(), Config.getDataInProgressPath(), filename, ".csv");

        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(Config.getSeparator())
                .withQuoteChar(Config.getQuoteChar())
                .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER)
                .build();

        try (
                CSVReader reader = new CSVReaderBuilder(
                        new FileReader(Config.getDataInProgressPath() + filename + ".csv"))
                        .withCSVParser(csvParser).build()) {

            CSVMetaData metaData = new CSVMetaData(reader);
            String[] fileColumns = reader.readNext();

            while (true) {
                String[] cursorData = reader.readNext();
                if (cursorData == null) {
                    break;
                }

                try {
                    Record csvRow = new Record(fileColumns, cursorData, filename, metaData);
                    DataExchanger.put(csvRow);
                } catch (Exception e){
                    warnLogger.error(e);
                }

            }


        } catch (Exception e) {
            warnLogger.error(e);
        } finally {
            StaticMethods.safeMoveFile(Config.getDataInProgressPath(), Config.getViewedDataPath(), filename, ".csv");
        }
    }

}
