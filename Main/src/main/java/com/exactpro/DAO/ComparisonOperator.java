package com.exactpro.DAO;

/**
 * Comparison enums for where condition in hibernate auto-generated queries
 */
public enum ComparisonOperator {
    EQUAL("equal to"),
    NOT_EQUAL("not equal to"),
    GREATER_THAN("greater than"),
    GREATER_THAN_OR_EQUAL("greater or equal than"),
    LESS_THAN("less than"),
    LESS_THAN_OR_EQUAL("less or equal than");

    private final String stringValue;

    ComparisonOperator(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString(){
        return stringValue;
    }
}
