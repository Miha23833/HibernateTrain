package com.exactpro.crutches;

import java.sql.*;

public class DBConnection {
    private static String url = "jdbc:mysql://localhost:3306/hibernate";
    private static String user = "root";
    private static String password = "password";

    public static void setConnectionData(String newUrl, String newUser, String newPassword) throws RuntimeException{
        if (newUrl == null || newUser == null || newPassword == null || newUrl.equals("") || newUser.equals(""))
            throw new RuntimeException("Empty or null values are not allowed");

        url = newUrl;
        user = newUser;
        password = newPassword;
    }

    public static ResultSet executeWithResult(String query) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        ResultSet resultSet;
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new SQLException();
        }

        return resultSet;
    }
}
