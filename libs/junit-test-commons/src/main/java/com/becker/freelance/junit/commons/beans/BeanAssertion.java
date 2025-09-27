package com.becker.freelance.junit.commons.beans;

import org.opentest4j.AssertionFailedError;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class BeanAssertion {

    private final BeanInfo beanInfo;
    private final Object expectedBean;

    public BeanAssertion(Object expectedBean) {

        assertNotNull(expectedBean, "expected bean");
        this.expectedBean = expectedBean;

        try {
            beanInfo = Introspector.getBeanInfo(expectedBean.getClass());
        } catch (IntrospectionException e) {
            throw new AssertionFailedError("BeanInfo", e);
        }
    }

    public static void assertBeanClassEquals(Object expectedBean, Object actualBean) {

        Class<?> expectedBeanClass = expectedBean.getClass();
        Class<?> actualBeanClass = actualBean.getClass();
        assertEquals(expectedBeanClass, actualBeanClass, "bean class");
    }

    public void assertBeanEquals(Object actualBean) {

        assertNotNull(actualBean, "actual bean");

        assertBeanClassEquals(expectedBean, actualBean);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            assertPropertyEquals(propertyDescriptor, actualBean);
        }
    }

    private void assertPropertyEquals(PropertyDescriptor propertyDescriptor, Object actualBean) {

        Method readMethod = propertyDescriptor.getReadMethod();

        try {
            Object expectedPropertyValue = readMethod.invoke(expectedBean);
            Object actualPropertyValue = readMethod.invoke(actualBean);

            Class<?> propertyType = propertyDescriptor.getPropertyType();
            if (propertyType.isArray()) {
                assertArrayEquals(
                        (Object[]) expectedPropertyValue,
                        (Object[]) actualPropertyValue,
                        propertyMsg(propertyDescriptor));
            } else {
                assertEquals(expectedPropertyValue, actualPropertyValue, propertyMsg(propertyDescriptor));
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            Throwable cause = e.getCause();
            if (e instanceof InvocationTargetException) {
                cause = ((InvocationTargetException) e).getTargetException();
            }
            throw new AssertionFailedError("Unable to read property " + propertyDescriptor.getName(), cause);
        }
    }

    private Supplier<String> propertyMsg(PropertyDescriptor propertyDescriptor) {

        return () -> {
            String propertyName = propertyDescriptor.getName();
            BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
            Class<?> beanClass = beanDescriptor.getBeanClass();
            return beanClass.getName() + "." + propertyName;
        };
    }
}
