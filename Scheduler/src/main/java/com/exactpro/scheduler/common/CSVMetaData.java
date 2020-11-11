package com.exactpro.scheduler.common;

import com.exactpro.scheduler.config.Config;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CSVMetaData {

    private final Map<String, String> metadata = new HashMap<>();

    public CSVMetaData(CSVReader reader) throws IOException, CsvValidationException {

        while (isMetaRow(reader.peek())) {
            StringBuilder builder = new StringBuilder(reader.peek()[0].replaceFirst(Config.getCsvMetaPredicate(), ""));

            int delimiterPos = builder.indexOf(Config.getCsvMetaKeyValueDelimiter());
            builder.replace(delimiterPos, delimiterPos + Config.getCsvMetaKeyValueDelimiter().length(), "=");

            String rowValue = builder.toString();

            if (rowValue.substring(delimiterPos).length() > 0 && delimiterPos > 0) {
                metadata.put(
                        rowValue.substring(0, delimiterPos),
                        rowValue.substring(delimiterPos + 1)
                );
            }
            reader.readNext();

        }
    }

    public String getOrDefault(String key, String defaultValue) {
        return metadata.getOrDefault(key, defaultValue);
    }

    public String get(String key) {
        return metadata.get(key);
    }

    public Set<String> keySet() {
        return metadata.keySet();
    }

    public static boolean isMetaRow(String[] row) {
        if (row == null || row.length != 1){
            return false;
        }

        String rowValue = row[0];

        return rowValue.startsWith(Config.getCsvMetaPredicate()) &&
                rowValue.contains(Config.getCsvMetaKeyValueDelimiter());

    }
}
