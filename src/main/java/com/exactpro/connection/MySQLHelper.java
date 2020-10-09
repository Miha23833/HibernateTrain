package com.exactpro.connection;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLHelper implements DBHelper {
    private final static Logger logger = Logger.getLogger(MySQLHelper.class);

    void setLogger(){
    }

    @Override
    public void createTable(String tableName, String[] columns) throws SQLException, ClassNotFoundException {
        if (columns.length%2 != 0)
            throw new RuntimeException("Missing column name or column data type");

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < columns.length; i+=2) {
            stringBuilder
                    .append('`')
                    .append(columns[i])
                    .append('`')
                    .append(' ')
                    .append(columns[i+1]);
            if (i != columns.length -2 )
                stringBuilder.append(',');
        }
        try {
            DBConnection.executeNonResult(String.format("CREATE TABLE `%s` (%s)", tableName, stringBuilder.toString()));

            logger.info("Table " + tableName + " created");

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error trying to create table", e);
            if (e.getCause() instanceof SQLException)
                throw new SQLException(e);
            else if (e.getCause() instanceof ClassNotFoundException)
                throw new ClassNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public void dropTable(String tableName) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.executeNonResult(String.format("DROP TABLE `%s`", tableName));

            logger.info("Table dropped " + tableName);

        } catch (ClassNotFoundException e) {
            logger.error("Error trying to drop table", e);
            if (e.getCause() instanceof SQLException)
                throw new SQLException(e);
            else if (e.getCause() instanceof ClassNotFoundException)
                throw new ClassNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public void addColumn(String tableName, String colName, String dataType) throws SQLException, ClassNotFoundException {

        try {
            DBConnection.executeNonResult(String.format(
                    "ALTER TABLE `%s` ADD COLUMN `%s` %s", tableName, colName, dataType));

            logger.info(String.format("Column %s added with type %s to table %s", colName, dataType, tableName ));

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error trying to add column", e);
            if (e.getCause() instanceof SQLException)
                throw new SQLException(e);
            else if (e.getCause() instanceof ClassNotFoundException)
                throw new ClassNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public void dropColumn(String colName, String tableName) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.executeNonResult(String.format(
                    "ALTER TABLE `%s` DROP COLUMN `%s`", tableName, colName));

            logger.info(String.format("Column %s dropped from table %s", colName, tableName));

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error trying to drop column", e);
            if (e.getCause() instanceof SQLException)
                throw new SQLException(e);
            else if (e.getCause() instanceof ClassNotFoundException)
                throw new ClassNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public void addTrigger(String trigName, String tableName, ExecTime execTime, Operation operation, String body) throws SQLException, ClassNotFoundException {

        try {
            DBConnection.executeNonResult(String.format(
                    "CREATE TRIGGER `%s`" +
                    "%s %s ON `%s` FOR EACH ROW " +
                    "%s", trigName, execTime, operation, tableName, body));

            logger.info(String.format("Trigger %s created on table %s", trigName, tableName));

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error trying to create trigger", e);
            if (e.getCause() instanceof SQLException)
                throw new SQLException(e);
            else if (e.getCause() instanceof ClassNotFoundException)
                throw new ClassNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public void dropTrigger(String trigName) throws SQLException, ClassNotFoundException {

        try {
            DBConnection.executeNonResult(String.format("DROP TRIGGER `%s`", trigName));

            logger.info("Trigger " + trigName + " dropped");

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error trying to drop trigger", e);
            if (e.getCause() instanceof SQLException)
                throw new SQLException(e);
            else if (e.getCause() instanceof ClassNotFoundException)
                throw new ClassNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public ResultSet selectData(String[] fields, String sourceTables, String condition)
            throws SQLException, ClassNotFoundException {
        ResultSet result = null;
        try {
            if (!condition.equals(""))
                condition = " WHERE " + condition;
            String joinedFields = String.join(",", fields);
            String query = "SELECT " + joinedFields + " FROM " + sourceTables + condition;
            result = DBConnection.executeWithResult(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error trying to select data", e);
            if (e.getCause() instanceof SQLException)
                throw new SQLException(e);
            else if (e.getCause() instanceof ClassNotFoundException)
                throw new ClassNotFoundException(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public void insertData(String table, String fields, String[] data) throws SQLException, ClassNotFoundException {
        try {
            for (int i = 0; i < data.length; i++) {
                if (!data[i].startsWith("("))
                    data[i] = "(" + data[i];
                if (!data[i].endsWith(")"))
                    data[i] = data[i] + ")";
            }

            String query = "INSERT INTO " + table + " (" + fields + ") VALUES " + String.join(",", data);
            DBConnection.executeNonResult(query);

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error trying to insert data", e);
            if (e.getCause() instanceof SQLException)
                throw new SQLException(e);
            else if (e.getCause() instanceof ClassNotFoundException)
                throw new ClassNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public void updateData(String table, String settingData, String condition) throws SQLException, ClassNotFoundException {
        try {
            if (!condition.equals(""))
                condition = " WHERE " + condition;
            String query = "UPDATE " + table + " SET " + settingData + condition;
            DBConnection.executeNonResult(query);

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error trying to update data", e);
            if (e.getCause() instanceof SQLException)
                throw new SQLException(e);
            else if (e.getCause() instanceof ClassNotFoundException)
                throw new ClassNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteData(String table, String condition) throws SQLException, ClassNotFoundException {
        try {
            if (!condition.equals(""))
                condition = " WHERE " + condition;

            String query = "DELETE FROM " + table + condition;
            DBConnection.executeNonResult(query);

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error trying to delete data", e);
            if (e.getCause() instanceof SQLException)
                throw new SQLException(e);
            else if (e.getCause() instanceof ClassNotFoundException)
                throw new ClassNotFoundException(e.getMessage(), e);
        }
    }
}
