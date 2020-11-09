package com.exactpro.scheduler.dataReader;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.common.StaticMethods;
import com.exactpro.scheduler.config.CSVEntityTypes;
import com.exactpro.scheduler.config.Config;
import org.apache.log4j.Logger;

import java.io.IOException;

public class DataReader {
    protected final Logger infoLogger = StaticLogger.infoLogger;
    protected final Logger warnLogger = StaticLogger.warnLogger;

    protected final String[] csvColumns;

    public DataReader(CSVEntityTypes entity) throws IOException {
        csvColumns = Config.getCSVColumns(entity);
        StaticMethods.createFolders( new String[]{Config.getFreshDataPath(), Config.getDataInProgressPath(), Config.getInsertedDataPath(), Config.getRejectedDataPath()});
    }

}
