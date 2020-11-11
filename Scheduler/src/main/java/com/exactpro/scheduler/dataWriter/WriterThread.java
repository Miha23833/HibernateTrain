package com.exactpro.scheduler.dataWriter;

import com.exactpro.DAO.GenericDAO;
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

        try {
            for (Record deal: dataToSave) {
                GenericDAO.insertEntity(session, deal);
            }
        }
        finally {
            if (session.getTransaction().isActive()){
                session.getTransaction().commit();
            }
            if (session.isOpen()){
                session.close();
            }
            infoLogger.info("Data was saved. Size is "+dataToSave.size());
        }
    }
}
