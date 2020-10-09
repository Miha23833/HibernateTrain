package com.exactpro.crutches;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Crutches {
    public static int getID(String tableName, String columnName) throws SQLException, ClassNotFoundException {
        String query = "SELECT MAX(`" + columnName + "`) as `maxvalue` FROM `" + tableName + "`;";
        ResultSet resultSet = DBConnection.executeWithResult(query);
        resultSet.next();
        return resultSet.getInt("maxvalue");
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println(getID("customers", "customer_id"));
    }
}

