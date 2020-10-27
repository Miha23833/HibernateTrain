package com.exactpro.scheduler;

import com.exactpro.loggers.StaticLogger;
import com.opencsv.CSVReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Scanner {
    private final Logger infoLogger = StaticLogger.infoLogger;
    private final Logger warnLogger = StaticLogger.warnLogger;

    private final String sourceRoot;

    private String freshData = "/ToInsert";
    private String insertedData = "/Done";
    private String rejectedData = "/Rejected";

    private final String delimiter;

    private void checkRelatively(String path) throws IOException {
        File file = new File(path);
        if (file.isAbsolute()){
            throw new IOException(String.format("Path %s is not relative.", path));
        }
    }

    private void createFolders() throws IOException {
        String logMsg = "Path %s created because it did not exist.";

        try {
            if (!Files.exists(Paths.get(sourceRoot + freshData))) {
                Files.createDirectories(Paths.get(sourceRoot + freshData));

                infoLogger.info(String.format(logMsg, sourceRoot + freshData));
            }

            if (!Files.exists(Paths.get(sourceRoot + insertedData))) {
                Files.createDirectories(Paths.get(sourceRoot + insertedData));

                infoLogger.info(String.format(logMsg, sourceRoot + freshData));
            }

            if (!Files.exists(Paths.get(sourceRoot + rejectedData))) {
                Files.createDirectories(Paths.get(sourceRoot + rejectedData));

                infoLogger.info(String.format(logMsg, sourceRoot + freshData));
            }

        } catch (IOException e) {
            warnLogger.error(e);
            throw new IOException(e);
        }
    }

    public Scanner (String delimiter, String sourceRoot, String freshData, String insertedData, String rejectedData) throws IOException {

        this.delimiter = delimiter;

        for (String value: new String[] {sourceRoot, freshData, insertedData, rejectedData}) {
            if (!value.startsWith("/")){
                value = "/"+value;
            }
            checkRelatively(value);
        }

        this.sourceRoot = sourceRoot;

        this.freshData = freshData;
        this.insertedData = insertedData;
        this.rejectedData = rejectedData;
    }

    public Scanner(String delimiter, String sourceRoot) throws IOException {
        this.delimiter = delimiter;

        checkRelatively(sourceRoot);
        this.sourceRoot = sourceRoot;
    }

    private List<String[]> loadCSV() throws FileNotFoundException {

        FilenameFilter filter = (dir, name) -> name.endsWith(".csv");
        File currentPath = new File(sourceRoot + freshData);
        String [] filenames = currentPath.list(filter);



    }

}
