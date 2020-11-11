package com.exactpro.scheduler;

import com.exactpro.scheduler.dataReader.DataReader;
import com.exactpro.scheduler.dataWriter.DataWriter;

public class Main {
    public static void main(String[] args) {
        Thread dataReader = new Thread(new DataReader());
        Thread dataWriter = new Thread(new DataWriter());

        dataReader.start();
        dataWriter.start();
    }
}
