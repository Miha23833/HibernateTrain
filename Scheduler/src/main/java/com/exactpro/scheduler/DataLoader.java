package com.exactpro.scheduler;

import com.exactpro.loggers.StaticLogger;
import com.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

/**
 * Loads data from csv to database
 */
public abstract class DataLoader {

    private final Logger warnLogger = StaticLogger.warnLogger;

    protected String[] columns;

    abstract void insertData(Session session, String path, String fileName, char delimiter) throws SQLException, ClassNotFoundException;

    protected ResultSet getDataFromCSV(String path, String fileName, char separator) throws ClassNotFoundException, SQLException {
        if (fileName.contains(".") && fileName.endsWith("csv")){
            fileName = fileName.replace(".csv", "");
        }
        Class.forName("org.relique.jdbc.csv.CsvDriver");

        Properties props = new Properties();
        props.put("separator", separator);

        Connection conn = DriverManager.getConnection("jdbc:relique:csv:" + path, props);
        Statement stmt = conn.createStatement();

        return stmt.executeQuery("SELECT " + String.join(",", columns) + " FROM " + fileName);
    }

    protected void saveDataToCSV(ResultSet data, String path, String fileName, char separator) throws SQLException, IOException {

        if (!fileName.startsWith("/")) {
            fileName = "/" + fileName;
        }
        if(path.length() > 0 && !path.endsWith("/")){
            path = path.substring(0, path.length()-1);
        }

        String namePostfix = "";
        int existingFilesCount = 0;
        while (Files.exists(Paths.get(path + fileName))){
            namePostfix = String.format("(%s)", ++existingFilesCount);
        }
        fileName = fileName + namePostfix;

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(path + fileName), separator, CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.RFC4180_LINE_END)) {
            csvWriter.writeAll(data, true);
        }catch (SQLException e){
            warnLogger.error(e);
            throw new SQLException(e);
        }
    }


}