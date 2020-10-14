package com.exactpro.DAO;

import org.hibernate.HibernateError;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static com.exactpro.loggers.StaticLogger.infoLogger;
import static com.exactpro.loggers.StaticLogger.warnLogger;

public class SingleSessionFactory {

    private volatile static SessionFactory instance;

    public static SessionFactory getInstance(){
        if (instance == null){
            synchronized (SingleSessionFactory.class){
                if (instance == null)
                    try {
                        instance = new Configuration().configure().buildSessionFactory();
                        infoLogger.info("New instance of SessionFactory was created");
                    }catch (HibernateException e){
                        warnLogger.error("Failed to create SessionFactory object", e);
                    }
            }
        }
        return instance;
    }

}
