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
            if(value.length() > 0 && !value.endsWith("/")){
                value = value.substring(0, value.length()-1);
            }
            checkRelatively(value);
        }

        this.sourceRoot = sourceRoot;

        this.freshData = freshData;
        this.insertedData = insertedData;
        this.rejectedData = rejectedData;

        createFolders();
    }

    public Scanner(String delimiter, String sourceRoot) throws IOException {
        this.delimiter = delimiter;

        checkRelatively(sourceRoot);
        this.sourceRoot = sourceRoot;

        createFolders();
    }

    public void scanAndLoadData() throws ClassNotFoundException, IOException {

        FilenameFilter filter = (dir, name) -> name.endsWith(".csv");
        File currentPath = new File(sourceRoot + freshData);
        String [] filenames = currentPath.list(filter);

        AbstractDataLoader loader = new DealDataLoader();

        Session session = SingleSessionFactory.getInstance().openSession();
        for (String filename : filenames) {
            try {
                loader.insertData(session, sourceRoot + freshData, filename);

                Files.move(Paths.get(sourceRoot + freshData + filename + ".csv"),
                        Paths.get(sourceRoot + insertedData),
                        StandardCopyOption.REPLACE_EXISTING);

            }catch (SQLException e){
                warnLogger.error(e);

                Files.move(Paths.get(sourceRoot + freshData + filename + ".csv"),
                        Paths.get(sourceRoot + rejectedData),
                        StandardCopyOption.REPLACE_EXISTING);

                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }

            } catch (ClassNotFoundException e) {
                warnLogger.error(e);
                throw new ClassNotFoundException(e.toString());
            }
        }
        if (session.getTransaction().isActive()) {
            session.getTransaction().commit();
        }
        session.close();
    }

}
