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

    private String freshData = "/ToInsert";
    private String insertedData = "/Done";
    private String rejectedData = "/Rejected";

    private final char delimiter;

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
                if (!Files.exists(Paths.get(path))) {
                    Files.createDirectories(Paths.get(path));

                    infoLogger.info(String.format(logMsg, path));
                }
            }

        } catch (IOException e) {
            warnLogger.error(e);
            throw new IOException(e);
        }
    }

    public Scanner(char delimiter, String sourceRoot, String freshData, String insertedData, String rejectedData) throws IOException {

        this.delimiter = delimiter;

        for (String value: new String[]{sourceRoot, freshData, insertedData, rejectedData}) {
            if (!value.startsWith("/") && !value.startsWith(".")) {
                value = "/" + value;
            }
            if (value.length() > 0 && !value.endsWith("/")) {
                value = value + "/";
            }
            checkRelatively(value);
        }

        this.sourceRoot = sourceRoot;

        this.freshData = freshData;
        this.insertedData = insertedData;
        this.rejectedData = rejectedData;

        createFolders();
    }

    public Scanner(char delimiter, String sourceRoot) throws IOException {
        this.delimiter = delimiter;

        checkRelatively(sourceRoot);
        this.sourceRoot = sourceRoot;

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

        DataLoader loader = new DealDataLoader();

        filename = filename.replace(".csv", "");


        Session session = SingleSessionFactory.getInstance().openSession();
        try {
            loader.insertData(session, sourceRoot + freshData, filename, delimiter);

            Files.move(Paths.get(sourceRoot + freshData + "/" + filename + ".csv"),
                    Paths.get(sourceRoot + insertedData + "/" + filename + ".csv"),
                    StandardCopyOption.REPLACE_EXISTING);

        } catch (SQLException e) {
            warnLogger.error(e);

            Files.move(Paths.get(sourceRoot + freshData + "/" + filename + ".csv"),
                    Paths.get(sourceRoot + rejectedData + "/" + filename + ".csv"),
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
