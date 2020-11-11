package com.exactpro.scheduler.dataReader;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.StaticMethods;
import com.exactpro.scheduler.config.Config;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataReader {
    protected static final Logger infoLogger = StaticLogger.infoLogger;
    protected static final Logger warnLogger = StaticLogger.warnLogger;

    public static void main(String[] args) throws InterruptedException, IOException {
        infoLogger.info("DataReader was start");
        ExecutorService threadPool = Executors.newFixedThreadPool(Config.getDataReaderMaxThreadPool());

        StaticMethods.createFolders(new String[]{
                Config.getFreshDataPath(),
                Config.getDataInProgressPath(),
                Config.getInsertedDataPath(),
                Config.getRejectedDataPath(),
                Config.getRootPath()
        });

        while (true) {
            for (String filename: StaticMethods.getCSVFilenamesInFolder(Config.getFreshDataPath())) {
                threadPool.execute(new ReaderThread(filename));
            }

            Thread.sleep(Config.getScannerPause());
        }

    }
}
