package com.exactpro.scheduler;

import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class Scanner {
    private final Logger infoLogger = StaticLogger.infoLogger;
    private final Logger warnLogger = StaticLogger.warnLogger;

    private final String sourceRoot;

    private final String freshData;
    private final String insertedData;
    private final String rejectedData;

    private final char delimiter;

    private String normalizePath(String path) throws IOException {
        if (path.length() > 0 && !path.endsWith("/")) {
            path = path + "/";
        }
        checkRelatively(path);
        return path;
    }

    private String addPostfixIfFileExists(String path, String filename, String extension){
        if (!extension.startsWith(".")){
            extension = "." + extension;
        }

        int existingFilesCounter = 1;
        String template = filename + " (%s)";
        while (Files.exists(Paths.get(path + "/" + filename + extension))){
            filename =  String.format(template, existingFilesCounter++);
        }
        return filename;
    }

    private void checkRelatively(String path) throws IOException {
        File file = new File(path);
        if (file.isAbsolute()) {
            throw new IOException(String.format("Path %s is not relative.", path));
        }
    }

    private void createFolders() throws IOException {
        String logMsg = "Path %s created because it did not exist.";

        try {
            for (String path: new String[]{freshData, insertedData, rejectedData}) {
                if (!Files.exists(Paths.get(sourceRoot + path))) {
                    Files.createDirectories(Paths.get(sourceRoot + path));

                    infoLogger.info(String.format(logMsg, sourceRoot + path));
                }
            }

        } catch (IOException e) {
            warnLogger.error(e);
            throw new IOException(e);
        }
    }

    public Scanner(char delimiter, String sourceRoot, String freshData, String insertedData, String rejectedData) throws IOException {

        this.delimiter = delimiter;

        this.sourceRoot = normalizePath(sourceRoot);

        this.freshData = normalizePath(freshData);
        this.insertedData = normalizePath(insertedData);
        this.rejectedData = normalizePath(rejectedData);

        createFolders();
    }

    public Scanner(char delimiter, String sourceRoot) throws IOException {
        this.delimiter = delimiter;

        checkRelatively(sourceRoot);
        this.sourceRoot = sourceRoot;

        this.freshData = "/ToInsert";
        this.insertedData = "/Done";
        this.rejectedData = "/Rejected";

        createFolders();
    }

    public String[] getCSVFilenames() {
        FilenameFilter filter = (dir, name) -> name.endsWith(".csv");
        File currentPath = new File(sourceRoot + freshData);
        String[] csvFilenames = currentPath.list(filter);
        if (csvFilenames == null){
            csvFilenames = new String[]{};
        }
        return csvFilenames;
    }

    /**
     * Looking for csv files in {@link #freshData} and loads them to database.
     * If data in file is not valid then replace it to {@link #rejectedData}.
     *
     * @throws ClassNotFoundException if no driver is found
     */
    public void loadDealsFromCSV(String filename) throws ClassNotFoundException, IOException {

        DataWorker loader = new DealDataWorker();

        filename = filename.replace(".csv", "");

        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();
        try {
            loader.insertData(session, sourceRoot + freshData, filename);

            String filenameWithPostfix = addPostfixIfFileExists(sourceRoot + insertedData, filename, ".csv");

            Files.move(Paths.get(sourceRoot + freshData + "/" + filename + ".csv"),
                    Paths.get(sourceRoot + insertedData + "/" + filenameWithPostfix + ".csv"),
                    StandardCopyOption.REPLACE_EXISTING);

        } catch (SQLException e) {
            warnLogger.error(e);

            String filenameWithPostfix = addPostfixIfFileExists(sourceRoot + rejectedData, filename, ".csv");

            // TODO: Процесс не может получить доступ к файлу, так как этот файл занят другим процессом
            Files.move(Paths.get(sourceRoot + freshData + "/" + filename + ".csv"),
                    Paths.get(sourceRoot + rejectedData + "/" + filenameWithPostfix + ".csv"),
                    StandardCopyOption.REPLACE_EXISTING);

            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }

        } catch (ClassNotFoundException e) {
            warnLogger.error(e);
            throw new ClassNotFoundException(e.toString());

        } finally {
            if (session.getTransaction().isActive()) {
                session.getTransaction().commit();
            }
            session.close();
        }
    }

}
