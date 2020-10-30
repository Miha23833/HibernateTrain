package com.exactpro.runnable;

import com.exactpro.functional.Function;
import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class SemaphoreThread<T> extends Thread {

    final Function<T> function;
    final T value;
    final Semaphore semaphore;

    Logger warnLogger = StaticLogger.warnLogger;

    public SemaphoreThread(Function<T> function, T value, Semaphore semaphore){
        this.function = function;
        this.value = value;
        this.semaphore = semaphore;
    }
    @Override
    public void run() {
        try {
            semaphore.acquire();
            function.execute(value);
        } catch (InterruptedException | IOException | ClassNotFoundException e) {
            warnLogger.error(e);
            throw new RuntimeException(e);
        }
        finally {
            semaphore.release();
        }
    }
}
