package com.exactpro.site.querymanager;

//import com.exactpro.loggers.StaticLogger;

import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QueryManager {
    public static class SelectQueries{
        public final String customers;
        public final String deals;
        public final String products;

        public SelectQueries(){
            customers = readFile(Paths.get(queryPath + "select/entities/Customers.sql"));
            deals = readFile(Paths.get(queryPath + "select/entities/Deals.sql"));
            products = readFile(Paths.get(queryPath + "select/entities/Products.sql"));
        }
    }

    public static class InsertQueries{
        public final String customers;
        public final String deals;
        public final String products;

        public InsertQueries(){
            customers = readFile(Paths.get(queryPath + "insert/entities/Customers.sql"));
            deals = readFile(Paths.get(queryPath + "insert/entities/Deals.sql"));
            products = readFile(Paths.get(queryPath + "insert/entities/Products.sql"));
        }
    }

    static private final Logger warnLogger = StaticLogger.warnLogger;

    private static final String queryPath = "src/main/java/com/exactpro/site/querymanager/queries/";

    public static final SelectQueries SELECT_QUERIES = new SelectQueries();

    public static final InsertQueries INSERT_QUERIES = new InsertQueries();

    private static String readFile(Path path){
        String data = "";
        try{
            data = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            warnLogger.error(e);
        }
        return data;
    }
}
