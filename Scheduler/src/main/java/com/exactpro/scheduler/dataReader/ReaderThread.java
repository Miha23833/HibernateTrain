package com.exactpro.scheduler.dataReader;

import com.exactpro.loggers.StaticLogger;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    /**
     * Adds number in brackets after filename if file with that name exists.
     *
     * @param path      path to scan for existing filename.
     * @param filename  default name of file.
     * @return filename with postfix like "file (20)" without extension
     */
    private static String addPostfixIfFileExists(String path, String filename){
        int existingFilesCounter = 1;
        String template = filename + " (%s)";
        while (Files.exists(Paths.get(path + "/" + filename + ".csv"))){
            filename =  String.format(template, existingFilesCounter++);
        }
        return filename;
    }

    private static void moveFile(String PathFrom, String PathTo, String filename) throws IOException {

        String filenameWithPostfix = addPostfixIfFileExists(PathTo, filename);

        Files.move(Paths.get(String.join("", PathFrom, filename + ".csv")),
                Paths.get(String.join("", PathTo, filenameWithPostfix + ".csv")),
                StandardCopyOption.REPLACE_EXISTING);
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

    @Override
    public void run() {
        try {
            moveFile(Config.getFreshDataPath(), Config.getDataInProgressPath(), processFile);

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
                moveFile(Config.getDataInProgressPath(), Config.getRejectedDataPath(), processFile);
            } catch (IOException ioException) {
                warnLogger.error(ioException);
            }
        }
    }

}
