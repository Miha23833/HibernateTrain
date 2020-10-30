package com.exactpro.scheduler;

import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Starter {

    static final Logger warnLogger = StaticLogger.warnLogger;
    static final Logger infoLogger = StaticLogger.infoLogger;


    public static void main(String[] args) throws IOException, InterruptedException {

        //TODO: Сделать автотесты
        Scheduler scheduler = new Scheduler();
        while (true) {
            scheduler.run();
            try {
                Thread.sleep(Config.getScannerPause());
            } catch (InterruptedException e) {
                warnLogger.error(e);
                throw new InterruptedException(e.toString());
            }
        }
    }
}
