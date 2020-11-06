package com.exactpro.scheduler;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.config.Config;
import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

/**
 * Loads data from csv to database
 */
public abstract class DataWorker {

    private final Logger warnLogger = StaticLogger.warnLogger;

    protected String[] columns;

    private String addPostfixNumberIfFileExists(String path, String filename){
        String fileExtension = ".csv";
        int existingFilesCounter = 1;
        String template = filename + " (%s)";
        while (Files.exists(Paths.get(path + "/" + filename + fileExtension))){
            filename =  String.format(template, existingFilesCounter++);
        }

        if (!filename.endsWith(fileExtension)){
            filename = filename + fileExtension;
        }

        return filename;
    }

    private String normalizeFilename(String filename){

        filename = filename.replace(".csv", "");

        if (!filename.startsWith("/")) {
            filename = "/" + filename;
        }

        return filename;
    }

    private String normalizePath(String path){
        if(path.length() > 0 && (path.endsWith("/") || path.endsWith("\\"))){
            path = path.substring(0, path.length()-1);
        }
        return path;
    }

    private boolean validColumns(String[] csvColumns){
        if (csvColumns == null || columns == null) {
            return false;
        }
        String[] copyColumns = Arrays.copyOf(columns, columns.length);
        String[] copyFileColumns = Arrays.copyOf(csvColumns, csvColumns.length);

        Arrays.sort(copyFileColumns);
        Arrays.sort(copyColumns);

        return Arrays.equals(copyFileColumns, copyColumns);
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

    abstract void insertData(Session session, String path, String filename) throws SQLException, ClassNotFoundException, IOException, CsvException;

    /**
     * Read data from csv file by given columns in deriving class
     * @param path relative path to file
     * @param filename name of file
     * @return SQLTable-like data from csv file
     */
    @Deprecated
    protected ResultSet getDataFromCSV(String path, String filename) throws ClassNotFoundException, SQLException {

        filename = normalizeFilename(filename);
        path = normalizePath(path);

        Class.forName("org.relique.jdbc.csv.CsvDriver");

        Properties props = new Properties();
        props.put("separator", String.valueOf(Config.getSeparator()));
        props.put("quotechar", String.valueOf(Config.getQuoteChar()));

        Connection conn = DriverManager.getConnection("jdbc:relique:csv:" + path, props);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String query = "SELECT " + String.join(",", columns) + " FROM " + String.format("\"%s\"",filename);

        return stmt.executeQuery(query);

    }


    /**
     * Read data from csv file by given columns in deriving class
     * @param path relative path to file
     * @param filename name of file
     * @return SQLTable-like data from csv file
     */
    protected Map <String, List<String>> getDataFromCSVReader(String path, String filename) throws IOException, CsvException {
        //TODO: читать файл частями. Размер частей задавать в конфиге

        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(Config.getSeparator())
                .withQuoteChar(Config.getQuoteChar()).build();

        try (CSVReader reader = new CSVReaderBuilder(
                new FileReader(path + filename + ".csv")).withCSVParser(csvParser).build()) {

            String[] fileColumns = reader.readNext();

            if (!validColumns(fileColumns)) {
                CsvValidationException exception = new CsvValidationException("File contains incorrect columns.");
                warnLogger.error(exception);
                throw exception;
            }

            List<String[]> resultCSV = new ArrayList<>();
            resultCSV.add(fileColumns);
            resultCSV.addAll(reader.readAll());

            return csvToHashmap(resultCSV);
        }
    }

    /**
     * Saves ResultSet to CSV. Returns new filename if previous was exist.
     * For example: file.csv -> file (1).csv
     * @param data data to insert.
     * @param path path for file.
     * @param filename name of file. Can be replaced and returned from this method.
     * @param separator separator of CSV file.
     * @return filename or filename if old was exist.
     */
    protected String saveDataToCSV(ResultSet data, String path, String filename, char separator) throws SQLException, IOException {

        filename = normalizeFilename(filename);
        path = normalizePath(path);

        filename = addPostfixNumberIfFileExists(path, filename);

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(path + filename), separator, CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.RFC4180_LINE_END)) {
            csvWriter.writeAll(data, true);
        }catch (SQLException e){
            warnLogger.error(e);
            throw new SQLException(e);
        }
        return filename;
    }


}