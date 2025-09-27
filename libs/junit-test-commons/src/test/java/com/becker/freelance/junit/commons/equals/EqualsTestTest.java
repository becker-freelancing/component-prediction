package com.becker.freelance.junit.commons.equals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class EqualsTestTest {

    EqualsTest equalsTest;

    Supplier<EqualsTestTestObject> baseObjectSupplier;
    EqualsTestTestObject baseObject;
    EqualsTestTestObject unequalBaseObject;

    @BeforeEach
    void setUp() {

        EqualsTest localEqualsTest = new EqualsTest() {

            @Override
            public Object baseObject() {

                throw new IllegalStateException("Method must be overwritten by spy");
            }

            @Override
            public Object unequalObject() {

                throw new IllegalStateException("Method must be overwritten by spy");
            }
        };
        equalsTest = spy(localEqualsTest);

        baseObjectSupplier = () -> new EqualsTestTestObject("Hello", 1, 2.3);
        baseObject = baseObjectSupplier.get();
        unequalBaseObject = new EqualsTestTestObject("Hello", 2, 2.3);

        doReturn(baseObject).when(equalsTest).baseObject();
        doReturn(unequalBaseObject).when(equalsTest).unequalObject();
    }

    @Test
    void testEqualsByPropertyThrowsErrorOnSameObjects() {

        assertThrows(AssertionFailedError.class, () -> equalsTest.testEqualsByProperty());
    }

    @Test
    void testEqualsByPropertyPassesOnEqualObjects() {

        EqualsTest equalsTest = new EqualsTest() {

            @Override
            public Object baseObject() {

                return new EqualsTestTestObject("Hello", 1, 2.3);
            }

            @Override
            public Object unequalObject() {

                throw new IllegalStateException("Method must be overwritten by spy");
            }
        };

        equalsTest.testEqualsByProperty();
    }

    @Test
    void testNotEqualOnDifferentPropertiesPassesOnDifferentProperties() {

        equalsTest.testNotEqualsOnDifferentProperties();
    }

    @Test
    void testNotEqualOnDifferentPropertiesFailsOnSameProperties() {

        doReturn(baseObjectSupplier.get()).when(equalsTest).unequalObject();

        assertThrows(AssertionFailedError.class, () -> equalsTest.testNotEqualsOnDifferentProperties());
    }

    @Test
    void testNotEqualsOnNullPasses() {

        equalsTest.testNotEqualsToNull();
    }

    @Test
    void testNotEqualToOtherClassPasses() {

        equalsTest.testNotEqualsToOtherClass();
    }

    @Test
    void testEqualsToItselfPasses() {

        equalsTest.testEqualsToItself();
    }
}
