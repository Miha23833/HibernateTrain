package com.exactpro.scheduler.dataWriter;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.config.Config;
import com.exactpro.scheduler.dataExchanger.DealExchanger;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataWriter implements Runnable {
    static final Logger infoLogger = StaticLogger.infoLogger;
    static final Logger warnLogger = StaticLogger.warnLogger;

    @Override
    public void run() {
        infoLogger.info("DataWriter was start");


        while (true) {
            ExecutorService threadPool = Executors.newFixedThreadPool(Config.getDataWriterMaxThreadPool());

            for (int i = 0;
                    i < (int)(((float) DealExchanger.size() / (float) Config.getDealExchangerCapacity()) * 10) ;
                    i++) {
                threadPool.execute(new WriterThread());
            }

            threadPool.shutdown();
        }
    }
}
