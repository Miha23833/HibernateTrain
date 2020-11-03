package com.exactpro.scheduler;

import java.io.*;
import java.util.Properties;


// load data from scanner.properties
public class Config {

    private static final String sourceRoot;

    private static final String freshData;
    private static final String insertedData;
    private static final String rejectedData;

    static final int scannerPause;

    private static final char separator;
    private static final char quoteChar;

    private static final int maxThreadPool;


    static {
        String resourceName = "scanner.properties";

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sourceRoot = props.getProperty("sourceRoot");
        freshData = props.getProperty("freshData");
        insertedData = props.getProperty("insertedData");
        rejectedData = props.getProperty("rejectedData");
        separator = props.getProperty("delimiter").toCharArray()[0];
        quoteChar = props.getProperty("quoteChar").toCharArray()[0];
        scannerPause = Integer.parseInt(props.getProperty("scannerPause"));
        maxThreadPool = Integer.parseInt(props.getProperty("maxThreadPool"));
    }

    public static String getFreshData(){
        return freshData;
    }

    public static String getInsertedData(){
        return insertedData;
    }

    public static String getRejectedData(){
        return rejectedData;
    }

    public static char getSeparator(){
        return separator;
    }

    public static char getQuoteChar(){
        return quoteChar;
    }

    public static String getSourceRoot() {
        return sourceRoot;
    }

    public static int getScannerPause(){
        return scannerPause;
    }

    public static int getMaxThreadPool(){
        return maxThreadPool;
    }
}
