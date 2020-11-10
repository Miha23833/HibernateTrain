package com.exactpro.scheduler.dataExchanger;

import com.exactpro.entities.Deal;
import com.exactpro.scheduler.config.Config;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DealExchanger{

    private final static int capacity = Config.getDataKeeperCapacity();
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
}
