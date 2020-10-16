package com.exactpro.cache;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.collections.LimitedSizeHashmap;
import com.exactpro.entities.Deal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

// TODO сделать прокси на все DAO с сессиями и т.п.

/**
 * Cache is working for only by dealId
 */
public class DealService implements Cache {

    private static final SessionFactory sf = SingleSessionFactory.getInstance();

    private static final LimitedSizeHashmap<Integer, Deal> cache = new LimitedSizeHashmap<>(50);

    public static synchronized void insertDeal(Deal deal){
        Session session = sf.openSession();
        session.beginTransaction();

        GenericDAO.insertEntity(session, deal);

        session.getTransaction().commit();
        session.close();

        cache.put(deal.getDealID(), deal);
    }

    public static synchronized Deal getByID(Integer id){
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        Session session = sf.openSession();
        Deal deal = GenericDAO.selectByID(session, Deal.class, id);
        if (deal != null) {
            cache.put(id, deal);
        }
        return deal;
    }

    public static synchronized void updateDeal(Deal deal){
        Session session = sf.openSession();
        session.beginTransaction();

        GenericDAO.updateTableByEntity(session, deal);

        session.getTransaction().commit();
        session.close();

        cache.put(deal.getDealID(), deal);
    }

    public static synchronized void deleteDeal(Deal deal){
        Session session = sf.openSession();
        session.beginTransaction();

        GenericDAO.deleteEntityFromTable(session, deal);

        session.getTransaction().commit();
        session.close();

        cache.removeKey(deal.getDealID());

    }

    @Override
    public synchronized void clean() {
        cache.clear();
    }
}
