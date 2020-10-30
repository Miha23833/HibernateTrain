package com.exactpro.tests;

import com.exactpro.connection.DBConnection;
import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;

import java.sql.SQLException;

public class CommonUnitTests {

    private static final Logger infoLogger = StaticLogger.infoLogger;

    public static void cleanDB(String url, String user, String password) throws SQLException, ClassNotFoundException {
        DBConnection.setConnectionData(url, user, password);

        DBConnection.executeNonResult("DELETE FROM CUSTOMERS");
        DBConnection.executeNonResult("ALTER TABLE CUSTOMERS AUTO_INCREMENT = 0");
        DBConnection.executeNonResult("DELETE FROM PRODUCTS");
        DBConnection.executeNonResult("ALTER TABLE PRODUCTS AUTO_INCREMENT = 0");
        DBConnection.executeNonResult("DELETE FROM DEALS");
        DBConnection.executeNonResult("ALTER TABLE DEALS AUTO_INCREMENT = 0");

        infoLogger.info("Database " + url + " was cleaned by user " + user);

    }
}
