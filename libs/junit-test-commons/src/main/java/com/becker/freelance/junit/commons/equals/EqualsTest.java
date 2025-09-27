package com.becker.freelance.junit.commons.equals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public interface EqualsTest<T> {

    public T baseObject();

    public T unequalObject();

    @Test
    @DisplayName("Test Object should be equal when properties are equal")
    default void testEqualsByProperty() {

        T expected = baseObject();
        T actual = baseObject();

        assertNotSame(
                expected,
                actual,
                "Object returned by methode \"baseObject\" should not be the same by reference (Use new Keyword in implementation)");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Object should not be equal when properties are not equal")
    default void testNotEqualsOnDifferentProperties() {

        T baseObject = baseObject();
        T unequalObject = unequalObject();

        assertNotEquals(baseObject, unequalObject);
    }

    @Test
    @DisplayName("Test Object is not equal to null")
    @SuppressWarnings("squid:S2159")
    default void testNotEqualsToNull() {

        T instance = baseObject();

        assertFalse(instance.equals(null));
    }

    @Test
    @DisplayName("Test Object is not equal to other class")
    default void testNotEqualsToOtherClass() {

        T instance = baseObject();
        Object otherObject = new DummyObject() {
        };

        assertFalse(otherObject.getClass().isInstance(instance));
        assertFalse(instance.equals(otherObject));
    }

    @Test
    @DisplayName("Test Object is equal to itself")
    @SuppressWarnings("squid:S1764")
    default void testEqualsToItself() {

        T instance = baseObject();

        assertTrue(instance.equals(instance));
    }


    @Test
    @DisplayName("Hashcode is differnt")
    default void testNotEqualsToDifferentHashCode() {
        T instance = baseObject();
        T differentObject = unequalObject();
        assertNotEquals(instance.hashCode(), differentObject.hashCode());
    }
}
