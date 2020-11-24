package com.exactpro.site.querymanager;

//import com.exactpro.loggers.StaticLogger;

import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QueryManager {
    static private final Logger warnLogger = StaticLogger.warnLogger;

    private static final String queryPath = "src/main/java/com/exactpro/site/querymanager/queries/";

    public static final String customersSQL;
    public static final String dealsSQL;
    public static final String productsSQL;

    private static String readFile(Path path){
        String data = "";
        try{
            data = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            warnLogger.error(e);
        }
        return data;
    }

    static {
        customersSQL = readFile(Paths.get(queryPath + "Customers.sql"));
        dealsSQL = readFile(Paths.get(queryPath + "Deals.sql"));
        productsSQL = readFile(Paths.get(queryPath + "Products.sql"));
    }
}
