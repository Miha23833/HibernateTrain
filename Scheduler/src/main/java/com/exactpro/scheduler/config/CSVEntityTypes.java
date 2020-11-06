package com.exactpro.scheduler.config;

public enum CSVEntityTypes {
    CUSTOMER("customerColumns"),
    PRODUCT("productColumns"),
    DEAL("dealColumns");

    private final String stringValue;
    CSVEntityTypes(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
