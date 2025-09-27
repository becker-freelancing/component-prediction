package com.becker.freelance.junit.commons.beans;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanPropertyAssertionTest {

    @Test
    void testDifferentBeansAreEqualWithSameProperties() {

        Bean expected = new Bean();
        Bean2 actual = new Bean2();

        new BeanPropertyAssertion(expected).assertBeanEquals(actual);
    }

    @Test
    void testDifferentBeansAreNotEqualWithDifferentArrayValues() {

        Bean expected = new Bean();
        Bean2 actual = new Bean2();

        actual.longValues = new Long[]{Long.MAX_VALUE};

        assertThrows(AssertionFailedError.class, () -> new BeanPropertyAssertion(expected).assertBeanEquals(actual));
    }

    @Test
    void testDifferentBeansAreNotEqualWithDifferentEnumValues() {

        Bean expected = new Bean();
        Bean2 actual = new Bean2();

        actual.enumValue = Enum2.VALUE_1;

        assertThrows(AssertionFailedError.class, () -> new BeanPropertyAssertion(expected).assertBeanEquals(actual));
    }

    @Test
    void testDifferentBeansAreNotEqualWithDifferentValues() {

        Bean expected = new Bean();
        Bean2 actual = new Bean2();

        actual.intValue = 12;

        assertThrows(AssertionFailedError.class, () -> new BeanPropertyAssertion(expected).assertBeanEquals(actual));
    }

    @Test
    void testThrowsAssertionFailedErrorWhenPropertyIsNotAccessible() {

        Bean expected = new Bean();
        Bean2 actual = new Bean2() {

            @Override
            public int getIntValue() {

                throw new IllegalStateException("TEST");
            }
        };

        actual.intValue = 12;

        assertThrows(AssertionFailedError.class, () -> new BeanPropertyAssertion(expected).assertBeanEquals(actual));
    }

    private enum Enum {
        VALUE_1,
        VALUE_2
    }

    private enum Enum2 {
        VALUE_1,
        VALUE_2
    }

    private class Bean {

        private int intValue = 1;
        private Long[] longValues = new Long[]{1L, Long.MAX_VALUE, Long.MIN_VALUE};
        private Enum enumValue = Enum.VALUE_2;

        public Enum getEnumValue() {

            return enumValue;
        }

        public int getIntValue() {

            return intValue;
        }

        public Long[] getLongValues() {

            return longValues;
        }

    }

    private class Bean2 {

        private int intValue = 1;
        private Long[] longValues = new Long[]{1L, Long.MAX_VALUE, Long.MIN_VALUE};
        private Enum2 enumValue = Enum2.VALUE_2;

        public Enum2 getEnumValue() {

            return enumValue;
        }

        public int getIntValue() {

            return intValue;
        }

        public Long[] getLongValues() {

            return longValues;
        }

    }

}