package com.exactpro.scheduler.dataWriter;

import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.Record;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.List;


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
        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();
        int savedDataCount = 0;

        try {
            for (Record dataRow: dataToSave) {
                if (!dataRow.getMetaData().containsKey("tableName")){
                    System.out.println("not contains tableName");
                    continue;
                }
                try {
                    String query = String.format("INSERT INTO %s ( %s ) VALUES ( %s )",
                            dataRow.getMetaData().get("tableName"),
                            String.join(", ", dataRow.getColumns()),
                            String.join(", ", dataRow.getData()));
                    session.createQuery(query).executeUpdate();

                    infoLogger.info("Query " + query + " was executed.");
                    savedDataCount++;
                } catch (Exception e){
                    e.printStackTrace();
                    warnLogger.error(e);
                }
            }
        }
        finally {
            if (session.getTransaction().isActive()){
                session.getTransaction().commit();
            }
            if (session.isOpen()){
                session.close();
            }
            infoLogger.info(String.join(" ","Data was saved. Size is ", Integer.toString(savedDataCount), "."
                    , "Rejected data size: ", String.valueOf(dataToSave.size() - savedDataCount)));
        }
    }
}
