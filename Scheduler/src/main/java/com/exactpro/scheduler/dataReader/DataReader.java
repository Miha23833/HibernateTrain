package com.exactpro.scheduler.dataReader;

import com.exactpro.loggers.StaticLogger;
import com.exactpro.scheduler.config.CSVEntityTypes;
import com.exactpro.scheduler.config.Config;
import org.apache.log4j.Logger;

public class DataReader {
    protected final Logger infoLogger = StaticLogger.infoLogger;
    protected final Logger warnLogger = StaticLogger.warnLogger;

    protected final String[] csvColumns;

    public DataReader(CSVEntityTypes entity) {
        csvColumns = Config.getCSVColumns(entity);
    }


}
