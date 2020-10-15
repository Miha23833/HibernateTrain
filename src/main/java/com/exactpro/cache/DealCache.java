package com.exactpro.cache;

import com.exactpro.DAO.ComparisonOperator;
import com.exactpro.DAO.DealDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.collections.LimitedSizeHashmap;
import com.exactpro.collections.tuple.TwoParameterTuple;
import com.exactpro.entities.Deal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.util.List;

// При инсерте нужно сбрасывать кэш.

public class DealCache implements Cache {

    private static final SessionFactory sf = SingleSessionFactory.getInstance();

    // Кэши для каждого из запросов
    private final static LimitedSizeHashmap
            <TwoParameterTuple<Long, ComparisonOperator>
                    , List<Deal>> cacheByDate = new LimitedSizeHashmap<>(50);

    private final static LimitedSizeHashmap<
            TwoParameterTuple<BigDecimal, ComparisonOperator>
            , List<Deal>> cacheByDiscount = new LimitedSizeHashmap<>(50);

    private final static LimitedSizeHashmap<
            TwoParameterTuple<BigDecimal, ComparisonOperator>
            , List<Deal>> cacheByPrice = new LimitedSizeHashmap<>(50);

    private final static LimitedSizeHashmap<Integer, List<Deal>> cacheByCustomerID = new LimitedSizeHashmap<>(50);

    private final static LimitedSizeHashmap<Integer, List<Deal>> cacheByProductID = new LimitedSizeHashmap<>(50);

    private final static LimitedSizeHashmap<
            TwoParameterTuple<Long, Long>
            , List<Deal>> cacheByPeriod = new LimitedSizeHashmap<>(50);


    public static synchronized List<Deal> getByDate(Long date, ComparisonOperator operator) {

        TwoParameterTuple<Long, ComparisonOperator> tuple = new TwoParameterTuple<>(date, operator);
        if (cacheByDate.containsKey(tuple)) {
            return cacheByDate.get(tuple);
        }
        else {
            Session session = sf.openSession();
            List<Deal> result = DealDAO.getByDate(session, date, operator);
            session.close();
            if (result.size() > 0) {
                cacheByDate.put(tuple, result);
            }
            return result;
        }

    }

    public static synchronized List<Deal> getByDiscount(BigDecimal discount, ComparisonOperator operator) {
        TwoParameterTuple<BigDecimal, ComparisonOperator> tuple = new TwoParameterTuple<>(discount, operator);
        if (cacheByDiscount.containsKey(tuple)) {
            return cacheByDiscount.get(tuple);
        }
        else {
            Session session = sf.openSession();
            List<Deal> result = DealDAO.getByDiscount(session, discount, operator);
            session.close();
            if (result.size() > 0) {
                cacheByDiscount.put(tuple, result);
            }
            return result;
        }
    }

    public static synchronized List<Deal> getByPrice(BigDecimal price, ComparisonOperator operator) {
        TwoParameterTuple<BigDecimal, ComparisonOperator> tuple = new TwoParameterTuple<>(price, operator);
        if (cacheByPrice.containsKey(tuple)) {
            return cacheByPrice.get(tuple);
        }
        else {
            Session session = sf.openSession();
            List<Deal> result = DealDAO.getByPrice(session, price, operator);
            session.close();
            if (result.size() > 0) {
                cacheByPrice.put(tuple, result);
            }
            return result;
        }
    }

    public static synchronized List<Deal> getByCustomerID(Integer customerID) {
        if (cacheByCustomerID.containsKey(customerID)) {
            return cacheByCustomerID.get(customerID);
        }
        else {
            Session session = sf.openSession();
            List<Deal> result = DealDAO.getByCustomerID(session, customerID);
            session.close();
            if (result.size() > 0) {
                cacheByCustomerID.put(customerID, result);
            }
            return result;
        }
    }

    public static synchronized List<Deal> getByProductID(Integer productID) {
        if (cacheByProductID.containsKey(productID)) {
            return cacheByProductID.get(productID);
        }
        else {
            Session session = sf.openSession();
            List<Deal> result = DealDAO.getByProductID(session, productID);
            session.close();
            if (result.size() > 0) {
                cacheByProductID.put(productID, result);
            }
            return result;
        }
    }

    public static synchronized List<Deal> getByPeriod(Long startRange, Long endRange) {
        TwoParameterTuple<Long, Long> ranges = new TwoParameterTuple<>(startRange, endRange);

        if (cacheByPeriod.containsKey(ranges)) {
            return cacheByPeriod.get(ranges);
        }
        else {
            Session session = sf.openSession();
            List<Deal> result = DealDAO.getByPeriod(session, startRange, endRange);
            session.close();
            if (result.size() > 0) {
                cacheByPeriod.put(ranges, result);
            }
            return result;
        }
    }


    @Override
    public void clean() {
        cacheByDate.removeAll();
        cacheByDiscount.removeAll();
        cacheByPrice.removeAll();
        cacheByCustomerID.removeAll();
        cacheByProductID.removeAll();
        cacheByPeriod.removeAll();
    }
}
