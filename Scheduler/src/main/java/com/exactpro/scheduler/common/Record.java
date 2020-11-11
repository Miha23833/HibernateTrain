package com.exactpro.scheduler.common;

public class Record {
    private final String[] columns;
    private final String[] data;
    private final String filename;
    private final CSVMetaData metaData;

    public Record(String[] columns, String[] data, String filename, CSVMetaData metaData) {
        this.columns = columns;
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
}
