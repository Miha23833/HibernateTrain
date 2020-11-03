package com.exactpro.functional;

import com.opencsv.exceptions.CsvException;

import java.io.IOException;

@FunctionalInterface
public interface Function<T> {
    void execute(T value) throws IOException, ClassNotFoundException, CsvException;
}
