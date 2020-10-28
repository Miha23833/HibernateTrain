package com.exactpro.connection;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBHelper {
    enum ExecTime{
        BEFORE,
        AFTER
    }

    enum Operation{
        INSERT,
        UPDATE,
        DELETE
    }

    void createTable(String tableName, String[] columns) throws SQLException, ClassNotFoundException;
    void dropTable(String tableName) throws SQLException, ClassNotFoundException;

    void addColumn(String tableName, String colName, String dataType) throws SQLException, ClassNotFoundException;
    void dropColumn(String colName, String tableName) throws SQLException, ClassNotFoundException;

    void addTrigger(String trigName, String triggeringTable, ExecTime execTime, Operation operation, String body) throws SQLException, ClassNotFoundException;
    void dropTrigger(String trigName) throws SQLException, ClassNotFoundException;

    ResultSet selectData(String[] fields, String sourceTables, String condition) throws SQLException, ClassNotFoundException;
    void insertData(String table, String fields, String[] data) throws SQLException, ClassNotFoundException;
    void updateData(String table, String settingData, String condition) throws SQLException, ClassNotFoundException;
    void deleteData(String table, String condition) throws SQLException, ClassNotFoundException;
}
