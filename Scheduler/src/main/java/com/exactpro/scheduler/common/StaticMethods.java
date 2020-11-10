package com.exactpro.scheduler.common;

import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

    public static String[] getCSVFilenamesInFolder(String path) {
        FilenameFilter filter = (dir, name) -> name.endsWith(".csv");
        File currentPath = new File(path);
        String[] csvFilenames = currentPath.list(filter);
        if (csvFilenames == null){
            csvFilenames = new String[]{};
        }
        return csvFilenames;
    }

    /**
     * Adds number in brackets after filename if file with that name exists.
     *
     * @param path      path to scan for existing filename.
     * @param filename  default name of file.
     * @return filename with postfix like "file (20)" without extension
     */
    private static String addPostfixIfFileExists(String path, String filename){
        int existingFilesCounter = 1;
        String template = filename + " (%s)";
        while (Files.exists(Paths.get(path + "/" + filename + ".csv"))){
            filename =  String.format(template, existingFilesCounter++);
        }
        return filename;
    }


    /**
     * Move file to other folder.
     * @param PathFrom current path to file.
     * @param PathTo target path to file.
     * @param filename name of file without extension.
     * @param extension extension
     * @throws IOException if moving is not finished correctly.
     * @apiNote file extension should not change.
     */
    public static void moveFile(String PathFrom, String PathTo, String filename, String extension) throws IOException {

        if(!extension.startsWith(".")){
            extension = "." + extension;
        }

        String filenameWithPostfix = addPostfixIfFileExists(PathTo, filename);

        Files.move(Paths.get(String.join("", PathFrom, filename + extension)),
                Paths.get(String.join("", PathTo, filenameWithPostfix + extension)),
                StandardCopyOption.REPLACE_EXISTING);
    }

}
