package com.exactpro.scheduler;

import com.exactpro.functional.Function;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.runnable.SemaphoreThread;
import com.exactpro.scheduler.config.Config;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Scheduler {

    final Scanner scanner;

    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    static final Logger warnLogger = StaticLogger.warnLogger;
    static final Logger infoLogger = StaticLogger.infoLogger;

    Set<String> foldersInProcessPool = new HashSet<>(){
        @Override
        public boolean remove(Object o) {
            try {
                readWriteLock.writeLock().lock();
                return super.remove(o);
            }
            finally {
                readWriteLock.writeLock().unlock();
            }
        }
    };

    final Semaphore semaphore;

    public Scheduler() throws IOException {

        scanner = new Scanner(Config.getSeparator(),
                Config.getRootPath(),
                Config.getFreshDataPath(),
                Config.getInsertedDataPath(),
                Config.getRejectedDataPath());

        if (Config.getDataReaderMaxThreadPool() < 1) {
            IllegalArgumentException exception = new IllegalArgumentException("MaxThreadPool must me at least 1.");
            warnLogger.error(exception);
            throw exception;
        }

        semaphore = new Semaphore(Config.getDataReaderMaxThreadPool());

    }

    public void run() {
        /*
        TODO: почитать про thread pool, schedule\delay
         */
        for (String fileName: scanner.getCSVFilenames()) {
            if (!foldersInProcessPool.contains(fileName)) {
                Function<String> lambda = scanner::loadDealsFromCSV;
                foldersInProcessPool.add(fileName);
                Thread CSVLoadThread = new SemaphoreThread<>(lambda, fileName, semaphore, foldersInProcessPool);
                CSVLoadThread.start();
                infoLogger.info(String.format("Thread %s started to load CSV to DB", CSVLoadThread.getName()));
            }
        }
    }

}
