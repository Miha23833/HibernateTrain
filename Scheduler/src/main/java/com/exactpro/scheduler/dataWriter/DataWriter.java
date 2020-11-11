package com.exactpro.scheduler.dataWriter;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.config.Config;
import com.exactpro.scheduler.dataExchanger.ConditionWaiter;
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
        ExecutorService threadPool = Executors.newFixedThreadPool(1);

        ConditionWaiter waiter = new ConditionWaiter(
                () -> (int)(((float) DealExchanger.size() / (float) Config.getDealExchangerCapacity()) * 100) > 70,
                Config.getScannerPause()
        );


        while (true) {
            try {
                waiter.await();
            } catch (InterruptedException e) {
                warnLogger.error(e);
            }
            threadPool.execute(new WriterThread());

            try {
                Thread.sleep(Config.getScannerPause());
            } catch (InterruptedException e) {
                warnLogger.error(e);
            }
        }

    }

}
