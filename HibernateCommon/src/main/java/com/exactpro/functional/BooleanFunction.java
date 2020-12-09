package com.exactpro.functional;

@FunctionalInterface
public interface BooleanFunction<T> {
    T execute();
}
