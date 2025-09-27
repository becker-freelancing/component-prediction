package com.becker.freelance.junit.commons.beans;

import org.opentest4j.AssertionFailedError;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class BeanPropertyAssertion {

    private final Object expectedBean;

    public BeanPropertyAssertion(Object expectedBean) {

        assertNotNull(expectedBean, "expected bean");
        this.expectedBean = expectedBean;
    }

    public void assertBeanEquals(Object actualBean) {

        assertNotNull(actualBean, "actual bean");

        Map<String, Method> expectedGetter = getGetter(expectedBean);
        Map<String, Method> actualGetter = getGetter(actualBean);

        assertTrue(expectedGetter.size() > 1, "More than one getter for " + expectedBean.getClass().getName());
        assertTrue(actualGetter.size() > 1, "More than one getter for " + actualBean.getClass().getName());

        for (Map.Entry<String, Method> entry : expectedGetter.entrySet()) {
            String expectedMethodName = entry.getKey();
            if (actualGetter.containsKey(expectedMethodName)) {
                assertPropertyEquals(
                        expectedGetter.get(expectedMethodName),
                        expectedBean,
                        actualGetter.get(expectedMethodName),
                        actualBean);
            }
        }
    }

    private void assertPropertyEquals(Method expectedGetter, Object expectedBean, Method actualGetter, Object actualBean) {

        if (expectedGetter.getName().equals("getClass")) {
            return;
        }

        try {
            Object expectedPropertyValue = expectedGetter.invoke(expectedBean);
            Object actualPropertyValue = actualGetter.invoke(actualBean);

            if (expectedPropertyValue == null && actualPropertyValue == null) {
                return;
            } else if (expectedPropertyValue == null) {
                throw new AssertionFailedError("Expected Property was null, but Actual was not null");
            }

            Class<?> propertyType = expectedPropertyValue.getClass();
            if (propertyType.isArray()) {
                assertArrayEquals(
                        (Object[]) expectedPropertyValue,
                        (Object[]) actualPropertyValue,
                        propertyMsg(expectedGetter));
            } else if (propertyType.isEnum()) {
                assertEquals(expectedPropertyValue.toString(), actualPropertyValue.toString(), propertyMsg(expectedGetter));
            } else {
                assertEquals(expectedPropertyValue, actualPropertyValue, propertyMsg(expectedGetter));
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            Throwable cause = e.getCause();
            if (e instanceof InvocationTargetException) {
                cause = ((InvocationTargetException) e).getTargetException();
            }
            throw new AssertionFailedError("Unable to read property " + expectedGetter.getName(), cause);
        }

    }

    private Map<String, Method> getGetter(Object expectedBean) {

        Map<String, Method> methods = new HashMap<>();

        Method[] declaredMethods = expectedBean.getClass().getMethods();

        for (Method method : declaredMethods) {
            String methodName = method.getName();
            if (methodName.startsWith("get")) {
                methods.put(methodName, method);
            }
        }

        return methods;
    }

    private Supplier<String> propertyMsg(Method expectedGetter) {

        return () -> {
            String propertyName = expectedGetter.getName().replace("get", "");
            Class<?> beanClass = expectedGetter.getDeclaringClass();

            return beanClass.getName() + "." + propertyName;
        };
    }

}
