package com.exactpro.scheduler;

import com.exactpro.functional.Function;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.runnable.SemaphoreThread;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class Scheduler {

    final Scanner scanner;

    static final Logger warnLogger = StaticLogger.warnLogger;
    static final Logger infoLogger = StaticLogger.infoLogger;

    Set<String> foldersInProcessPool = new HashSet<>();

    final Semaphore semaphore;

    public Scheduler() throws IOException {

        scanner = new Scanner(Config.getDelimiter(),
                Config.getSourceRoot(),
                Config.getFreshData(),
                Config.getInsertedData(),
                Config.getRejectedData());

        if (Config.getMaxThreadPool() < 1) {
            IllegalArgumentException exception = new IllegalArgumentException("MaxThreadPool must me at least 1.");
            warnLogger.error(exception);
            throw exception;
        }

        semaphore = new Semaphore(Config.getMaxThreadPool());

    }

    public void run() {
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
