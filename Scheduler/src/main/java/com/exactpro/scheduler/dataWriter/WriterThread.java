package com.exactpro.scheduler.dataWriter;

import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.Record;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


//TODO убрать работу с перемещением, ибо это должно быть заботой DataReader. Пусть только он валидирует.
public class WriterThread implements Runnable {

    private static final Logger warnLogger = StaticLogger.warnLogger;
    private static final Logger infoLogger = StaticLogger.infoLogger;
    private final List<Record> dataToSave;

    public WriterThread(List<Record> dataToSave) {
        this.dataToSave = dataToSave;
    }

    @Override
    public void run() {
        infoLogger.info(String.format("Thread %s was started", Thread.currentThread().getName()));
        Set<String> filenames = new HashSet<>();
        try (Session session = SingleSessionFactory.getInstance().openSession()) {
            int savedDataCount = 0;
            for (Record dataRow: dataToSave) {
                if (!dataRow.getMetaData().containsKey("tableName")) {

                    if (!filenames.contains(dataRow.getFilename())) {
                        infoLogger.warn("File " + dataRow.getFilename() + " does not contains tableName. Row ");
                        filenames.add(dataRow.getFilename());
                    }

                    continue;
                }
                try {
                    session.beginTransaction();
                    String query = String.format("INSERT INTO %s ( %s ) VALUES ( %s )",
                            dataRow.getMetaData().get("tableName"),
                            String.join(", ", dataRow.getColumns()),
                            String.join(", ", dataRow.getData()));
                    session.createNativeQuery(query).executeUpdate();
                    infoLogger.info("Query " + query + " was executed.");
                    savedDataCount++;
                } catch (Exception e) {
                    warnLogger.error(e);
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                } finally {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().commit();
                    }
                }
            }
            infoLogger.info(String.join(" ", "Data was saved. Size is ", Integer.toString(savedDataCount), "."
                    , "Rejected data size: ", String.valueOf(dataToSave.size() - savedDataCount)));
        }
    }
}
