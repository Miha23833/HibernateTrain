package com.exactpro.scheduler.dataExchanger;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.Record;
import com.exactpro.scheduler.config.Config;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataExchanger {

    private static final Logger warnLogger = StaticLogger.warnLogger;
    private final static int capacity = Config.getDataExchangerCapacity();
    private final static ArrayList<Record> data = new ArrayList<>();
    private final static ReadWriteLock lock = new ReentrantReadWriteLock();

    public static void put(Record dataRow) {
        try {
            while (data.size() >= capacity) {
                synchronized (data) {
                    data.wait();
                }
            }
        } catch (InterruptedException e) {
            warnLogger.error(e);
        }

        lock.writeLock().lock();
        try {
            data.add(dataRow);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static List<Record> getAllData() {
        lock.writeLock().lock();
        try {
            List<Record> copyData = new LinkedList<>(data);
            data.clear();
            return copyData;
        } finally {
            lock.writeLock().unlock();
            synchronized (data) {
                data.notifyAll();
            }
        }
    }

    public static Record getLast() {
        if (data.size() > 0) {
            Record lastOne = data.remove(data.size() - 1);
            synchronized (data) {
                data.notifyAll();
            }
            return lastOne;
        }
        else {
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

    public static List<Record> getPart() {
        if (size() == 0) {
            return new LinkedList<>();
        }

        lock.writeLock().lock();
        try {
            List<Record> partOfData = new LinkedList<>();
            int partSize = Config.getDataExchangerCapacity() / Config.getDataWriterMaxThreadPool();

            if (data.size() <= partSize) {
                List<Record> copyData = new LinkedList<>(data);
                data.clear();
                return copyData;
            }

            for (int i = 0; i < partSize; i++) {
                partOfData.add(data.remove(data.size() - 1));
            }
            return partOfData;
        } finally {
            lock.writeLock().unlock();
            synchronized (data) {
                data.notifyAll();
            }
        }
    }
}
