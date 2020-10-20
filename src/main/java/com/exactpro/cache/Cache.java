package com.exactpro.cache;

public interface Cache {
    /**
     * Cleans cache.
     */
    void clean();

    int getSize();

    int maxSize();
}
