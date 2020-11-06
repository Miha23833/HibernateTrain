package com.exactpro.scheduler.config;

import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


// load data from scanner.properties
public class Config {
    private static final Properties props;
    private static final Logger warnLogger = StaticLogger.warnLogger;

    private static final String rootPath;

    private static final String freshDataPath;
    private static final String dataInProgressPath;
    private static final String insertedDataPath;
    private static final String rejectedDataPath;

    static final int scannerPause;

    private static final char separator;
    private static final char quoteChar;

    private static final int maxThreadPool;

    private static final Map<String, String[]> csvColumns = new HashMap<>();


    static {
        String resourceName = "scanner.properties";

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        rootPath = props.getProperty("rootPath");
        freshDataPath = props.getProperty("freshDataPath");
        dataInProgressPath = props.getProperty("dataInProgressPath");
        insertedDataPath = props.getProperty("insertedDataPath");
        rejectedDataPath = props.getProperty("rejectedDataPath");
        separator = props.getProperty("delimiter").toCharArray()[0];
        quoteChar = props.getProperty("quoteChar").toCharArray()[0];
        scannerPause = Integer.parseInt(props.getProperty("scannerPause"));
        maxThreadPool = Integer.parseInt(props.getProperty("maxThreadPool"));
    }

    public static boolean containsKey(String key){
        return props.contains(key);
    }

    public static String getFreshDataPath(){
        return freshDataPath;
    }

    public static String getDataInProgressPath(){
        return dataInProgressPath;
    }

    public static String getInsertedDataPath(){
        return insertedDataPath;
    }

    public static String getRejectedDataPath(){
        return rejectedDataPath;
    }

    public static String getRootPath() {
        return rootPath;
    }

    public static char getSeparator(){
        return separator;
    }

    public static char getQuoteChar(){
        return quoteChar;
    }

    public static int getScannerPause(){
        return scannerPause;
    }

    public static int getMaxThreadPool(){
        return maxThreadPool;
    }

    public static String[] getCSVColumns(CSVEntityTypes entity){
        try {
            return csvColumns.get(entity.toString());
        }catch (NullPointerException e){
            warnLogger.error(e);
            throw e;
        }
    }

}
