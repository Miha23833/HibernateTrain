package com.exactpro.scheduler.common;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Record implements Comparable<Record> {
    private final String[] columns;
    private final String[] data;
    private final String filename;
    private final CSVMetaData metaData;

    public Record(String[] columns, String[] data, String filename, CSVMetaData metaData) {
        this.columns = columns;
        for (int i = 0; i < data.length; i++) {
            if (!data[i].startsWith("'")){
                data[i] = "'"+data[i];
            }
            if (!data[i].endsWith("'")){
                data[i] = data[i]+"'";
            }
        }
        this.data = data;
        this.filename = filename;
        this.metaData = metaData;
    }

    public String[] getColumns() {
        return columns;
    }

    public String[] getData() {
        return data;
    }

    public String getFilename() {
        return filename;
    }

    public CSVMetaData getMetaData(){
        return metaData;
    }

    @Override
    public int compareTo(@NotNull Record o) {
        return Arrays.compare(this.columns, o.getColumns());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Data: { ");
        for (int i = 0; i < data.length; i++) {
            builder.append(columns[i]).append(": ").append(data[i]);
            if (i != data.length - 1){
                builder.append(", ");
            }
        }
        builder.append(" } ");

        builder.append("Metadata: { ");
        for (String key : metaData.keySet()) {
            builder.append(key).append(": ").append(metaData.get(key)).append(", ");
        }
        builder.append("filename: ").append(filename);
        builder.append(" } ");

        return builder.toString();
    }
}
