package com.exactpro.scheduler.dataReader;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.config.CSVEntityTypes;
import com.exactpro.scheduler.config.Config;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class DataReader {
    protected final Logger infoLogger = StaticLogger.infoLogger;
    protected final Logger warnLogger = StaticLogger.warnLogger;

    protected final String rootPath;

    protected final String freshDataPath;
    protected final String inProgressDataPath;
    protected final String insertedDataPath;
    protected final String rejectedDataPath;

    protected final String[] csvColumns;

    /**
     * Creates folders in project.
     *
     * @param rootPath           root path.
     * @param freshDataPath      path that contains only files that awaiting for processing.
     * @param inProgressDataPath path that contains files that are already being processed.
     * @param insertedDataPath   path that contains files that are correct and already inserted in database.
     * @param rejectedDataPath   path that contains files that are not correct and not inserted in database.
     * @throws IOException if program cannot create paths.
     */
    public DataReader(String rootPath, String freshDataPath, String inProgressDataPath, String insertedDataPath, String rejectedDataPath, CSVEntityTypes entity) throws IOException {
        this.rootPath = normalizePath(rootPath);
        this.freshDataPath = normalizePath(freshDataPath);
        this.inProgressDataPath = normalizePath(inProgressDataPath);
        this.insertedDataPath = normalizePath(insertedDataPath);
        this.rejectedDataPath = normalizePath(rejectedDataPath);

        csvColumns = Config.getCSVColumns(entity);

        createFolders();
    }

    /**
     * Creates folders in project. Folder names will be get from config file.
     *
     * @throws IOException if program cannot create paths.
     */
    public DataReader(CSVEntityTypes entity) throws IOException {
        this(
                Config.getRootPath(),
                Config.getFreshDataPath(),
                Config.getDataInProgressPath(),
                Config.getInsertedDataPath(),
                Config.getRejectedDataPath(),
                entity
        );
    }

    /**
     * Adds slash to end of path.
     *
     * @param path relative path.
     * @return normalized path like "pathname/".
     * @throws IOException if path is not relative.
     */
    protected String normalizePath(String path) throws IOException {
        if (path.length() > 0 && !path.endsWith("/")) {
            path = path + "/";
        }
        checkRelatively(path);
        return path;
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

    protected void checkRelatively(String path) throws IOException {
        File file = new File(path);
        if (file.isAbsolute()) {
            throw new IOException(String.format("Path %s is not relative.", path));
        }
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

    protected String[] getFilesFromFresh() {
        FilenameFilter filter = (dir, name) -> name.endsWith(".csv");
        File currentPath = new File(rootPath + freshDataPath);
        String[] csvFilenames = currentPath.list(filter);
        if (csvFilenames == null) {
            csvFilenames = new String[]{};
        }
        return csvFilenames;
    }


}
