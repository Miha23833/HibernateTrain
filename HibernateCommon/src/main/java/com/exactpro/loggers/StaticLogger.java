package com.exactpro.loggers;

import org.apache.log4j.Logger;

public class StaticLogger {
    public static final Logger infoLogger = Logger.getLogger("infoLogger");
    public static final Logger warnLogger = Logger.getLogger("warnLogger");
    public static final Logger csvRejectedDataLogger = Logger.getLogger("rejectedCSVLogger");
}
