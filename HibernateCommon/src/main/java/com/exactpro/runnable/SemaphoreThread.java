package com.exactpro.runnable;

import com.exactpro.functional.Function;
import com.exactpro.loggers.StaticLogger;
import com.opencsv.exceptions.CsvException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class SemaphoreThread<T> extends Thread {

    final Function<T> function;
    final T value;
    final Semaphore semaphore;
    Set<T> pool;

    Logger warnLogger = StaticLogger.warnLogger;

    // TODO: поменять пул на папку inProgress
    public SemaphoreThread(Function<T> function, T value, Semaphore semaphore, Set<T> pool){
        this.function = function;
        this.value = value;
        this.semaphore = semaphore;
        this.pool = pool;
    }
    @Override
    public void run() {
        try {
            semaphore.acquire();
            function.execute(value);
        } catch (InterruptedException | IOException | ClassNotFoundException | CsvException e) {
            warnLogger.error(e);
            throw new RuntimeException(e);
        }
        finally {
            semaphore.release();
            pool.remove(value);
        }
    }
}
