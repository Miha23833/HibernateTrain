package com.exactpro.scheduler.dataWriter;

import com.exactpro.entities.Deal;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.config.Config;
import com.exactpro.scheduler.dataExchanger.DealExchanger;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataWriter implements Runnable {
    static final Logger infoLogger = StaticLogger.infoLogger;
    static final Logger warnLogger = StaticLogger.warnLogger;

    @Override
    public void run() {
        infoLogger.info("DataWriter was start");
        ExecutorService threadPool = Executors.newFixedThreadPool(Config.getDataWriterMaxThreadPool());

        while (true) {
            List<Deal> data =DealExchanger.getPart();

            if (data.size() > 0) {
                threadPool.execute(new WriterThread(data));
            }
            try {
                Thread.sleep(Config.getScannerPause());
            } catch (InterruptedException e){
                warnLogger.error(e);
            }
        }

    }

}
