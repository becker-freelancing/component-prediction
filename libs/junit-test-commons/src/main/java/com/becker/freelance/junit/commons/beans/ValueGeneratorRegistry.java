package com.becker.freelance.junit.commons.beans;

public interface ValueGeneratorRegistry {

    public ValueGenerator getValueGenerator(Class<?> type);
}
