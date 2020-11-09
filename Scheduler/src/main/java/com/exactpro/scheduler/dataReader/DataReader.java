package com.exactpro.scheduler.dataReader;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.StaticMethods;
import com.exactpro.scheduler.config.Config;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataReader {
    protected static final Logger infoLogger = StaticLogger.infoLogger;
    protected static final Logger warnLogger = StaticLogger.warnLogger;

    public static void main(String[] args) {
        final String[] csvColumns = Config.getCSVColumns();
        infoLogger.info(String.format("Data reader has start working with given columns: %s", String.join(",", csvColumns)));

        ExecutorService threadPool = Executors.newFixedThreadPool(Config.getMaxThreadPool());

        while (true){
            for (String filename : StaticMethods.getCSVFilenamesInFolder(Config.getFreshDataPath())) {
                threadPool.execute(new ReaderThread(filename, csvColumns) );
            }
        }

    }


}
