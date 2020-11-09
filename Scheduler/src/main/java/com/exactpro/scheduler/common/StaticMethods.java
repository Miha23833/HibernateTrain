package com.exactpro.scheduler.common;

import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StaticMethods {
    protected static final Logger infoLogger = StaticLogger.infoLogger;
    protected static final Logger warnLogger = StaticLogger.warnLogger;

    /**
     * Creates folders to work with files.
     *
     * @throws IOException if cannot crate path.
     */
    public static void createFolders(String[] folders) throws IOException {
        String logMsg = "Path %s created because it did not exist.";

        try {
            for (String path: folders) {
                if (!Files.exists(Paths.get(path))) {
                    Files.createDirectories(Paths.get(path));
                    infoLogger.info(String.format(logMsg, path));
                }
            }
        } catch (IOException e) {
            warnLogger.error(e);
            throw new IOException(e);
        }
    }

}
