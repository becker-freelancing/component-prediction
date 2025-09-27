package com.becker.freelance.junit.commons.beans;

import org.junit.jupiter.api.*;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Erstellt {@link DynamicTest}s, welche einfache Datenstruktur-Beans testen. Datenstruktur-Beans sind
 * Datenstruktur-Objekte, welche die Java-Bean-Spezifikation umsetzen. D.h. Klassen ohne Logik die nur Daten enthalten.
 * Da man für solche Klassen ungern Tests schreibt, existiert dieser {@link DynamicBeanTest}. Es wird getestet, dass
 * ein Getter-Aufruf den Wert des vorherigen Setter-Aufrufs zurückgibt. Falls die getestete Bean {@link #equals(Object)}
 * oder {@link #hashCode()} implementiert, werden diese auch getestet. Bei beiden werden alle Permutationen der
 * Properties getestet, sodass bei Bedingungen innerhalb der Methoden auch alle Branches abgedeckt werden.
 */
@SuppressWarnings("java:S112")
public abstract class DynamicBeanTest extends BeanTestBase {

    @TestFactory
    @DisplayName("Bean Tests")
    Stream<DynamicContainer> beanClassTests() {

        Stream<Class<?>> classStream = beanClasses();
        return classStream.map(this::beanClassTest);
    }

    protected abstract Stream<Class<?>> beanClasses();

    private DynamicContainer beanClassTest(Class<?> beanClass) {

        Stream<? extends DynamicNode> beanClassTestStream = beanClassTests(beanClass);
        return DynamicContainer.dynamicContainer(beanClass.getSimpleName(), beanClassTestStream);
    }

    private Stream<? extends DynamicNode> beanClassTests(Class<?> beanClass) {

        AtomicInteger testCounter = new AtomicInteger();
        Stream<? extends DynamicTest> defaultConstructorTest = defaultConstructorTest(beanClass, testCounter);
        Stream<? extends DynamicTest> propertyTests = propertyTests(beanClass, testCounter);
        Stream<? extends DynamicNode> equalsAndHashCodeTests = equalsAndHashCodeTests(beanClass, testCounter);
        Stream<? extends DynamicNode> toStringTests = toStringTests(beanClass, testCounter);

        return Stream.of(defaultConstructorTest, propertyTests, equalsAndHashCodeTests, toStringTests)
                .reduce(Stream::concat)
                .orElseGet(Stream::empty);
    }

    private Stream<? extends DynamicTest> defaultConstructorTest(Class<?> beanType, AtomicInteger testCounter) {

        return Stream.of(beanType).map(bc -> {
            String beanClassTestName = nextIndexLabel(testCounter) + " new " + beanType.getSimpleName() + "()";
            return DynamicTest.dynamicTest(beanClassTestName, () -> newInstance(beanType));
        });
    }

    private Stream<? extends DynamicTest> propertyTests(Class<?> beanClass, AtomicInteger testCounter) {

        try {
            Object bean = newInstance(beanClass);
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass, Object.class);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            return Arrays.asList(propertyDescriptors).stream().map(pd -> propertyTest(testCounter, bean, pd));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create dynamic property tests", e);
        }
    }

    private DynamicTest propertyTest(AtomicInteger testCounter, Object bean, PropertyDescriptor propertyDescriptor) {

        int nextValue = testCounter.getAndIncrement();
        String displayName = "[" + nextValue + "] Property " + propertyDescriptor.getName();
        return DynamicTest.dynamicTest(displayName, () -> testSetAndGetProperty(bean, propertyDescriptor));
    }

    private Stream<? extends DynamicNode> equalsAndHashCodeTests(Class<?> beanType, AtomicInteger testCounter) {

        try {
            beanType.getDeclaredMethod("equals", Object.class);

            return Stream.of(beanType).map(bc -> {
                        DynamicContainer equalsTest = DynamicContainer.dynamicContainer(
                                "equals",
                                Stream.of(
                                        DynamicTest
                                                .dynamicTest(nextIndexLabel(testCounter) + " equal to null", () -> testBeanEqualToNull(beanType)),
                                        DynamicTest
                                                .dynamicTest(nextIndexLabel(testCounter) + " equal to itself", () -> testBeanEqualToItself(beanType)),
                                        DynamicTest.dynamicTest(
                                                nextIndexLabel(testCounter) + " equal to incompatible type",
                                                () -> testBeanEqualToIncompatibleType(beanType)),
                                        DynamicTest.dynamicTest(
                                                nextIndexLabel(testCounter) + " equal to another bean with same state",
                                                () -> testBeanEqualToAnotherBeanWithSameState(beanType)),
                                        DynamicTest.dynamicTest(
                                                nextIndexLabel(testCounter) + " equal to another bean with all property permutations",
                                                () -> testForEachPropertyPermutation(beanType))));

                        DynamicContainer hashCodeTest = DynamicContainer.dynamicContainer(
                                "hashCode",
                                Stream.of(
                                        DynamicTest.dynamicTest(
                                                nextIndexLabel(testCounter) + " hashCode equal on equal beans",
                                                () -> testEqualHashCodeOnEqualBeans(beanType)),
                                        DynamicTest.dynamicTest(
                                                nextIndexLabel(testCounter) + " hashCode not equal on different beans",
                                                () -> testNonEqualHashCodeOnDifferentBeans(beanType))));

                        return Arrays.asList(equalsTest, hashCodeTest);
                    }

            ).flatMap(List::stream);
        } catch (NoSuchMethodException e) {
            return Stream.empty();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create dynamic property tests", e);
        }
    }

    private Stream<? extends DynamicNode> toStringTests(Class<?> beanType, AtomicInteger testCounter) {

        try {
            beanType.getDeclaredMethod("toString");

            return Stream.of(beanType).map(bc -> {
                        DynamicContainer toString = DynamicContainer.dynamicContainer(
                                "toString",
                                Stream.of(
                                        DynamicTest
                                                .dynamicTest(
                                                        nextIndexLabel(testCounter) + " toString contains all properties",
                                                        () -> testToStringContainsAllProperties(beanType)),
                                        DynamicTest
                                                .dynamicTest(
                                                        nextIndexLabel(testCounter) + " toString contains classname",
                                                        () -> testToStringContainsClassname(beanType))));

                        return List.of(toString);
                    }

            ).flatMap(List::stream);
        } catch (NoSuchMethodException e) {
            return Stream.empty();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create dynamic property tests", e);
        }
    }

    private String nextIndexLabel(AtomicInteger counter) {

        return "[" + counter.getAndIncrement() + "]";
    }
}
