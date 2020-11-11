package com.exactpro.scheduler.dataWriter;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.StaticMethods;
import com.exactpro.scheduler.config.Config;
import com.exactpro.scheduler.dataExchanger.DealExchanger;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataWriter {
    static final Logger infoLogger = StaticLogger.infoLogger;
    static final Logger warnLogger = StaticLogger.warnLogger;

    public static void main(String[] args) throws IOException, InterruptedException {
        StaticMethods.createFolders(new String[]{Config.getFreshDataPath(), Config.getDataInProgressPath(), Config.getInsertedDataPath(), Config.getRejectedDataPath()});

        final String[] csvColumns = Config.getCSVColumns();
        infoLogger.info(String.format("Data writer has start working with given columns: %s", String.join(",", csvColumns)));

        ExecutorService threadPool = Executors.newFixedThreadPool(Config.getDataWriterMaxThreadPool());

        while (true) {
            for (int i = 0;
                    i < (int) ((float) DealExchanger.size() / (float) Config.getDealExchangerCapacity()) * 10 ;
                    i++) {
                threadPool.execute(new WriterThread());
            }

            Thread.sleep(Config.getScannerPause());
        }

    }



}
