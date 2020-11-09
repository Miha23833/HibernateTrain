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

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class ReaderThread implements Runnable {

    private final String[] dataColumns;
    Logger warnLogger = StaticLogger.warnLogger;

    public ReaderThread(String[] dataColumns){
        this.dataColumns = dataColumns;
    }

    private String getFirstUncheckedFile() {
        FilenameFilter filter = (dir, name) -> name.endsWith(".csv");
        File currentPath = new File(Config.getFreshDataPath());
        String[] csvFilenames = currentPath.list(filter);
        if (csvFilenames.length < 1){
            return null;
        }
        return csvFilenames[0];
    }

    private boolean validColumns(String[] csvFileColumns){
        if (csvFileColumns == null || csvFileColumns.length < dataColumns.length){
            return false;
        }
        return Arrays.asList(csvFileColumns).containsAll(Arrays.asList(dataColumns));
    }

    private String addPostfixIfFileExists(String path, String filename){
        int existingFilesCounter = 1;
        String template = filename + " (%s)";
        while (Files.exists(Paths.get(path + "/" + filename + ".csv"))){
            filename =  String.format(template, existingFilesCounter++);
        }
        return filename;
    }

    private void moveFile(String PathFrom, String PathTo, String filename) throws IOException {

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
        String filename = getFirstUncheckedFile();

        try {
            moveFile(Config.getFreshDataPath(), Config.getDataInProgressPath(), filename);

            String filePath = Config.getDataInProgressPath();

            CSVParser csvParser = new CSVParserBuilder()
                    .withSeparator(Config.getSeparator())
                    .withQuoteChar(Config.getQuoteChar()).build();

            try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath + filename + ".csv")).withCSVParser(csvParser).build()) {

                String[] fileColumns = reader.readNext();

                if (!validColumns(fileColumns)) {
                    CsvValidationException exception = new CsvValidationException("File contains incorrect columns.");
                    warnLogger.error(exception);
                    throw exception;
                }

                List<String[]> resultCSV = new ArrayList<>();
                resultCSV.add(fileColumns);
                resultCSV.addAll(reader.readAll());

                FileDataKeeper.add(filename, csvToHashmap(resultCSV));
            }
        }catch (CsvException | IOException e) {
            warnLogger.error(e);
            try {
                moveFile(Config.getDataInProgressPath(), Config.getRejectedDataPath(), filename);
            } catch (IOException ioException) {
                warnLogger.error(ioException);
            }
        }
    }

}
