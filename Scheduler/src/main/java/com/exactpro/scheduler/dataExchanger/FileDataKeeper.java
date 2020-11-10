package com.exactpro.scheduler.dataExchanger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileDataKeeper<T>{

    ConditionWaiter waiter;
    private final List<T> data = new LinkedList<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public FileDataKeeper(int queueSize) {
        waiter  = new ConditionWaiter(() -> data.size() < queueSize, 300);
    }

    public void put(T entity) {
        try {
            waiter.await();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        lock.writeLock().lock();
        try {
            data.add(entity);
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    public List<T> getAllData(){
        lock.writeLock().lock();
        try {
            List<T> copyData = new LinkedList<>(data);
            data.clear();
            return copyData;
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    public T getLast(){
        if (data.size() > 0) {
            return data.remove(data.size() - 1);
        }
        else{
            return null;
        }
    }
}
