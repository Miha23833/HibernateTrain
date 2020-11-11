package com.exactpro.scheduler.dataExchanger;

import com.exactpro.entities.Deal;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.config.Config;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DealExchanger{

    private static Logger warnLogger = StaticLogger.warnLogger;
    private final static int capacity = Config.getDealExchangerCapacity();
    private final static List<Deal> data = new LinkedList<>();
    private final static ReadWriteLock lock = new ReentrantReadWriteLock();

    public static void put(Deal deal) {
        try {
            new ConditionWaiter(() -> data.size() < capacity, 300).await();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        lock.writeLock().lock();
        try {
            data.add(deal);
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    public static List<Deal> getAllData(){

        try {
            new ConditionWaiter(() -> (float) data.size() % (float) capacity > 50).await();
        } catch (InterruptedException e) {
            warnLogger.error(e);
        }

        lock.writeLock().lock();
        try {
            List<Deal> copyData = new LinkedList<>(data);
            data.clear();
            return copyData;
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    public static Deal getLast(){
        if (data.size() > 0) {
            return data.remove(data.size() - 1);
        }
        else{
            return null;
        }
    }

    public static int size() {
        lock.readLock().lock();
        try {
            return data.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
