package com.exactpro.scheduler;

import org.hibernate.Session;

import java.sql.*;

/**
 * Loads data from csv to database
 */
public abstract class AbstractDataLoader {
    protected String selectStatement;

    abstract void insertData(Session session, String path, String fileName) throws SQLException, ClassNotFoundException;

    protected ResultSet getDataFromCSV(String path, String fileName) throws ClassNotFoundException, SQLException {
        if (fileName.contains(".") && fileName.endsWith("csv")){
            fileName = fileName.replace(".csv", "");
        }
        Class.forName("org.relique.jdbc.csv.CsvDriver");
        Connection conn = DriverManager.getConnection("jdbc:relique:csv:" + path);
        Statement stmt = conn.createStatement();

        return stmt.executeQuery(selectStatement + " FROM " + fileName);
    }


}