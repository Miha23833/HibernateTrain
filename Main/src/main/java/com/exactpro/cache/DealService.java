package com.exactpro.cache;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.collections.LimitedSizeHashmap;
import com.exactpro.entities.Deal;
import com.exactpro.loggers.StaticLogger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// TODO сделать прокси на все DAO с сессиями и т.п.

/**
 * Cache is working for only by dealId
 */
public class DealService implements Cache {

    private static final SessionFactory sf = SingleSessionFactory.getInstance();

    private static final LimitedSizeHashmap<Integer, Deal> cache = new LimitedSizeHashmap<>(50);

    private static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static void insertDeal(Deal deal) {
        try {
            readWriteLock.writeLock().lock();

            Session session = sf.openSession();
            session.beginTransaction();

            GenericDAO.insertEntity(session, deal);

            session.getTransaction().commit();
            session.close();

            cache.put(deal.getDealID(), deal);

            StaticLogger.infoLogger.info(String.format("Deal with id = %s was put into cache", deal.getDealID()));
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public static Deal getByID(Integer id) {
        readWriteLock.readLock().lock();
        Session session = null;
        try {
            if (cache.containsKey(id)) {
                Deal deal = cache.get(id);
                StaticLogger.infoLogger.info(String.format("Deal with id = %s was taken from cache", deal.getDealID()));
                return deal;
            }
            session = sf.openSession();
            Deal deal = GenericDAO.selectByID(session, Deal.class, id);
            if (deal != null && deal.getDealID() != null) {
                try {
                    readWriteLock.readLock().unlock();
                    readWriteLock.writeLock().lock();

                    cache.put(id, deal);
                    StaticLogger.infoLogger.info(String.format("Deal with id = %s wasn't in cache. It was put into cache", deal.getDealID()));

                }
                finally {
                    readWriteLock.writeLock().unlock();
                    readWriteLock.readLock().lock();
                }
            }
            return deal;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
            readWriteLock.readLock().unlock();
        }

    }

    public static void updateDeal(Deal deal) {
        try {
            readWriteLock.writeLock().lock();

            Session session = sf.openSession();
            session.beginTransaction();

            GenericDAO.updateTableByEntity(session, deal);

            session.getTransaction().commit();
            session.close();

            cache.put(deal.getDealID(), deal);
            StaticLogger.infoLogger.info(String.format("Deal with id = %s was put into cache", deal.getDealID()));
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public static void deleteDeal(Deal deal) {
        try {
            readWriteLock.writeLock().lock();

            Session session = sf.openSession();
            session.beginTransaction();

            GenericDAO.deleteEntityFromTable(session, deal);

            session.getTransaction().commit();
            session.close();

            cache.removeKey(deal.getDealID());
            StaticLogger.infoLogger.info(String.format("Deal with id = %s was removed from cache", deal.getDealID()));
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }

    @Override
    public void clean() {
        try {
            readWriteLock.writeLock().lock();
            cache.clear();
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public int getSize() {
        try {
        readWriteLock.writeLock().lock();
        return cache.size();
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public int maxSize() {
        try {
        readWriteLock.writeLock().lock();
        return cache.maxSize();
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

}
