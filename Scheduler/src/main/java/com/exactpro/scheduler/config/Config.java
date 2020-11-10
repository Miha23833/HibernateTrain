package com.exactpro.scheduler.config;

import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


// load data from scanner.properties
public class Config {
    private static final Properties props;
    private static final Logger warnLogger = StaticLogger.warnLogger;

    private static String rootPath = null;

    private static String freshDataPath = null;
    private static String dataInProgressPath = null;
    private static String insertedDataPath = null;
    private static String rejectedDataPath = null;

    static final int scannerPause;

    private static final char separator;
    private static final char quoteChar;

    private static final int maxThreadPool;

    private static final String tableToInsert;
    private static final String[] csvColumns;


    private static void isRelatively(String path) throws IOException {
        File file = new File(path);
        if (file.isAbsolute()) {
            throw new IOException(String.format("Path %s is not relative.", path));
        }
    }

    /**
     * Adds slash to end of path.
     *
     * @param path relative path.
     * @return normalized path like "pathname/".
     * @throws IOException if path is not relative.
     */
    private static String normalizePath(String path) throws IOException {
        if (path.length() > 0 && !path.endsWith("/")) {
            path = path + "/";
        }
        isRelatively(path);
        return path;
    }

    static {
        String resourceName = "scanner.properties";

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            rootPath = normalizePath(props.getProperty("rootPath"));
            freshDataPath = normalizePath(String.join("", rootPath, props.getProperty("freshDataPath")));
            dataInProgressPath = normalizePath(String.join("", props.getProperty("dataInProgressPath")));
            insertedDataPath = normalizePath(String.join("", props.getProperty("insertedDataPath")));
            rejectedDataPath = normalizePath(String.join("", props.getProperty("rejectedDataPath")));
        } catch (IOException e){
            warnLogger.error(e);
            System.exit(-1);
        }

        separator = props.getProperty("delimiter").toCharArray()[0];
        quoteChar = props.getProperty("quoteChar").toCharArray()[0];
        scannerPause = Integer.parseInt(props.getProperty("scannerPause"));
        maxThreadPool = Integer.parseInt(props.getProperty("maxThreadPool"));

        tableToInsert = props.getProperty("tableToInsert");
        csvColumns = props.getProperty("dataColumns").split(",");
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

    public static String[] getCSVColumns(){
        return csvColumns;
    }

    public static String getTableToInsert(){
        return tableToInsert;
    }

}
