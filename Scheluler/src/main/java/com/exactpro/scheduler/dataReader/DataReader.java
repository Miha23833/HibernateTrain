package com.exactpro.scheduler.dataReader;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.StaticMethods;
import com.exactpro.scheduler.config.Config;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataReader implements Runnable{
    protected static final Logger infoLogger = StaticLogger.infoLogger;
    protected static final Logger warnLogger = StaticLogger.warnLogger;

    @Override
    public void run() {
        infoLogger.info("DataReader was start");

        StaticMethods.createFolders(new String[]{
                Config.getFreshDataPath(),
                Config.getDataInProgressPath(),
                Config.getViewedDataPath(),
                Config.getRootPath()
        });

        ExecutorService threadPool = Executors.newFixedThreadPool(Config.getDataReaderMaxThreadPool());

        while (true) {
            for (String filename: StaticMethods.getCSVFilenamesInFolder(Config.getFreshDataPath())) {
                threadPool.execute(new ReaderThread(filename));
            }

            try {
                Thread.sleep(Config.getScannerPause());
            } catch (InterruptedException e) {
                warnLogger.error(e);
            }
        }
    }
}
