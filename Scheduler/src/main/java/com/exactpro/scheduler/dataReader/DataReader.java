package com.exactpro.scheduler.dataReader;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.Scanner;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataReader {
    private final Logger infoLogger = StaticLogger.infoLogger;
    private final Logger warnLogger = StaticLogger.warnLogger;

    private final String rootPath;

    private final String freshDataPath;
    private final String inProgressDataPath;
    private final String insertedDataPath;
    private final String rejectedDataPath;


    public DataReader(String rootPath, String freshDataPath, String inProgressDataPath, String insertedDataPath, String rejectedDataPath) throws IOException {
        this.rootPath = rootPath;
        this.freshDataPath = freshDataPath;
        this.inProgressDataPath = inProgressDataPath;
        this.insertedDataPath = insertedDataPath;
        this.rejectedDataPath = rejectedDataPath;

        createFolders();
    }

    public DataReader(String rootPath) throws IOException {
        this(rootPath, "freshData", "dataInProgress", "insertedData", "rejectedData");
    }

    /**
     * Creates default folders in project
     * @throws IOException if program cannot create paths.
     */
    public DataReader() throws IOException {
        this("csvData");
    }

    private void createFolders() throws IOException {
        String logMsg = "Path %s created because it did not exist.";

        try {
            for (String path: new String[]{freshDataPath, inProgressDataPath, insertedDataPath, rejectedDataPath}) {
                if (!Files.exists(Paths.get(rootPath + path))) {
                    Files.createDirectories(Paths.get(rootPath + path));

                    infoLogger.info(String.format(logMsg, rootPath + path));
                }
            }

        } catch (IOException e) {
            warnLogger.error(e);
            throw new IOException(e);
        }
    }
}
