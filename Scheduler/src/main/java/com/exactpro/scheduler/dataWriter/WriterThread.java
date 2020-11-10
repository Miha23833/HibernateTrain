package com.exactpro.scheduler.dataWriter;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.entities.Deal;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.dataExchanger.DealExchanger;
import org.apache.log4j.Logger;
import org.hibernate.Session;


//TODO убрать работу с перемещением, ибо это должно быть заботой DataReader. Пусть только он валидирует.
public class WriterThread implements Runnable {

    private static final Logger warnLogger = StaticLogger.warnLogger;

    @Override
    public void run() {
        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();

        try {
            for (Deal deal: DealExchanger.getAllData()) {
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
        }
    }
}
