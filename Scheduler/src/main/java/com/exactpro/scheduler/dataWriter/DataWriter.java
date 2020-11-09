package com.exactpro.scheduler.dataWriter;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.StaticMethods;
import com.exactpro.scheduler.config.CSVEntityTypes;
import com.exactpro.scheduler.config.Config;
import org.apache.log4j.Logger;

import java.io.IOException;

public class DataWriter {
    protected final Logger infoLogger = StaticLogger.infoLogger;
    protected final Logger warnLogger = StaticLogger.warnLogger;

    private final String[] dataColumns;

    public DataWriter(CSVEntityTypes entity) throws IOException {
        dataColumns = Config.getCSVColumns();
        StaticMethods.createFolders( new String[]{Config.getFreshDataPath(), Config.getDataInProgressPath(), Config.getInsertedDataPath(), Config.getRejectedDataPath()});
    }



}
