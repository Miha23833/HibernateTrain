package com.exactpro.scheduler.dataReader;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.config.CSVEntityTypes;
import com.exactpro.scheduler.config.Config;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataReader {
    protected final Logger infoLogger = StaticLogger.infoLogger;
    protected final Logger warnLogger = StaticLogger.warnLogger;

    protected final String rootPath;

    protected final String freshDataPath;
    protected final String inProgressDataPath;
    protected final String insertedDataPath;
    protected final String rejectedDataPath;

    protected final String[] csvColumns;

    /**
     * Creates folders in project. Folder names will be get from config file.
     */
    public DataReader(CSVEntityTypes entity) {
        this.rootPath = Config.getRootPath();
        this.freshDataPath = Config.getFreshDataPath();
        this.inProgressDataPath = Config.getDataInProgressPath();
        this.insertedDataPath = Config.getInsertedDataPath();
        this.rejectedDataPath = Config.getRejectedDataPath();
        csvColumns = Config.getCSVColumns(entity);
    }


    /**
     * Adds number in brackets after filename if file with that name exists.
     *
     * @param path      path to scan for existing filename.
     * @param filename  default name of file.
     * @param extension file extension like ".csv".
     * @return filename with postfix like "file (20)" without extension
     */
    protected String addPostfixIfFileExists(String path, String filename, String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }

        int existingFilesCounter = 1;
        String template = filename + " (%s)";
        while (Files.exists(Paths.get(String.join("", path, "/", filename, extension)))) {
            filename = String.format(template, existingFilesCounter++);
        }
        return filename;
    }

    /**
     * Creates folders to work with files.
     *
     * @throws IOException if cannot crate path.
     */
    protected void createFolders() throws IOException {
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
