package com.exactpro.entities.functional;

import java.io.IOException;

@FunctionalInterface
public interface Function {
    void execute() throws IOException, ClassNotFoundException;
}
