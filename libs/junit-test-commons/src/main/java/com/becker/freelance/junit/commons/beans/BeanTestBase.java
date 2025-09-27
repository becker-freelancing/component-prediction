package com.becker.freelance.junit.commons.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("java:S112")
public abstract class BeanTestBase {

    private ValueGeneratorRegistry valueGeneratorRegistry = new ReflectiveValueGeneratorRegistry();

    public void setValueGeneratorRegistry(ValueGeneratorRegistry valueGeneratorRegistry) {

        this.valueGeneratorRegistry = requireNonNull(valueGeneratorRegistry);
    }

    protected void testSetAndGetProperty(Object bean, String propertyName) throws Exception {

        Map<String, PropertyDescriptor> properties = getProperties(bean.getClass());

        PropertyDescriptor property = properties.get(propertyName);
        if (property == null) {
            throw new IllegalArgumentException("No property " + propertyName + " exists in " + bean.getClass());
        }

        testSetAndGetProperty(bean, property);
    }

    protected void testSetAndGetProperty(Object bean, PropertyDescriptor propertyDescriptor) throws Exception {

        Method getter = propertyDescriptor.getReadMethod();
        Method setter = propertyDescriptor.getWriteMethod();
        Class<?> propertyType = propertyDescriptor.getPropertyType();
        ValueGenerator valueGenerator = valueGeneratorRegistry.getValueGenerator(propertyType);
        Object propertyValue = valueGenerator.getValue();

        setter.invoke(bean, propertyValue);
        assertEquals(propertyValue, getter.invoke(bean), () -> "setAndGet: " + propertyDescriptor.getName());
    }

    protected void testBeanEqualToIncompatibleType(Class<?> beanType) throws Exception {

        Object bean1 = newInstance(beanType);
        assertNotEquals(bean1, beanType, "incompatible type");
    }

    @SuppressWarnings("java:S2159")
    protected void testBeanEqualToNull(Class<?> beanType) throws Exception {

        Object bean1 = newInstance(beanType);
        Map<String, Object> equalState = generateBeanState(beanType);
        setState(bean1, equalState);
        assertFalse(bean1.equals(null), "bean.equals(null)");
    }

    protected void testBeanEqualToAnotherBeanWithSameState(Class<?> beanType) throws Exception {

        Object bean1 = newInstance(beanType);
        Map<String, Object> equalState = generateBeanState(beanType);
        setState(bean1, equalState);

        Object bean2 = clone(bean1);
        assertEquals(bean1, bean2, "objects with same state");
    }

    protected void testEqualHashCodeOnEqualBeans(Class<?> beanType) throws Exception {

        Object bean1 = newInstance(beanType);
        Map<String, Object> equalState = generateBeanState(beanType);
        setState(bean1, equalState);

        Object bean2 = clone(bean1);
        assertEquals(bean1.hashCode(), bean2.hashCode(), "hashCode");
    }

    protected void testNonEqualHashCodeOnDifferentBeans(Class<?> beanType) throws Exception {

        Object bean1 = newInstance(beanType);
        Map<String, Object> equalState = generateBeanState(beanType);
        setState(bean1, equalState);

        Object bean2 = newInstance(beanType);
        Map<String, Object> equalState2 = generateBeanState(beanType);
        setState(bean2, equalState2);
        assertEquals(bean2.hashCode(), bean2.hashCode(), "hashCode");
    }

    protected void testBeanEqualToItself(Class<?> beanType) throws Exception {

        Object bean1 = newInstance(beanType);
        Map<String, Object> equalState = generateBeanState(beanType);
        setState(bean1, equalState);
        assertEquals(bean1, bean1, "object with itself");
    }

    protected void testForEachPropertyPermutation(Class<?> beanType) throws Exception {

        Object bean = newInstance(beanType);
        Map<String, Object> beanState = generateBeanState(beanType);
        setState(bean, beanState);

        Map<String, PropertyDescriptor> properties = getProperties(beanType);

        for (Map.Entry<String, PropertyDescriptor> property : properties.entrySet()) {
            PropertyDescriptor propertyDescriptor = property.getValue();

            testUnequalProperty(bean, propertyDescriptor);
        }
    }

    private void testUnequalProperty(Object bean, PropertyDescriptor propertyDescriptor) throws Exception {

        testPropertyUnequalToNull(bean, propertyDescriptor);
        testPropertyUnequalToDifferentValue(bean, propertyDescriptor);
        testNullUnequalToProperty(bean, propertyDescriptor);
    }

    private void testPropertyUnequalToDifferentValue(Object bean, PropertyDescriptor propertyDescriptor) throws Exception {

        Object otherBean = clone(bean);
        assertEquals(bean, otherBean);
        Object equalValue = getProperty(bean, propertyDescriptor);
        Object unequalValue = generateUnequalValue(equalValue, propertyDescriptor);

        setProperty(otherBean, propertyDescriptor, unequalValue);

        String propertyName = propertyDescriptor.getName();
        Supplier<String> msg =
                () -> "Beans should not be equal by property " + propertyName + ": " + equalValue + "<>" + unequalValue;
        assertNotEquals(bean, otherBean, msg);
    }

    private void testPropertyUnequalToNull(Object bean, PropertyDescriptor propertyDescriptor) throws Exception {

        Object otherBean = clone(bean);
        assertEquals(bean, otherBean);

        setProperty(otherBean, propertyDescriptor, null);
        Object equalValue = getProperty(bean, propertyDescriptor);
        Object unequalValue = equalValue == null ? generateUnequalValue(null, propertyDescriptor) : equalValue;
        String propertyName = propertyDescriptor.getName();
        Supplier<String> msg =
                () -> "Beans should not be equal by property " + propertyName + ": " + equalValue + "<>" + unequalValue;
        assertNotEquals(bean, otherBean, msg);
    }

    private void testNullUnequalToProperty(Object bean, PropertyDescriptor propertyDescriptor) throws Exception {

        Object otherBean = bean;
        bean = clone(otherBean);
        assertEquals(bean, otherBean);

        setProperty(otherBean, propertyDescriptor, null);
        Object equalValue = getProperty(bean, propertyDescriptor);
        Object unequalValue = equalValue == null ? generateUnequalValue(null, propertyDescriptor) : equalValue;
        String propertyName = propertyDescriptor.getName();
        Supplier<String> msg =
                () -> "Beans should not be equal by property " + propertyName + ": " + equalValue + "<>" + unequalValue;
        assertNotEquals(bean, otherBean, msg);
    }

    protected Object generateUnequalValue(Object equalValue, PropertyDescriptor propertyDescriptor) {

        Class<?> propertyType = propertyDescriptor.getPropertyType();
        ValueGenerator valueGenerator = valueGeneratorRegistry.getValueGenerator(propertyType);

        for (int i = 0; i < 10; i++) {
            Object candidateValue = valueGenerator.getValue();
            if (!Objects.equals(equalValue, candidateValue)) {
                return candidateValue;
            }
        }

        if (equalValue != null) {
            return null;
        }

        throw new IllegalStateException("Unable to generate unequal value for " + propertyDescriptor);
    }

    private Object clone(Object bean) throws Exception {

        Object clone = newInstance(bean.getClass());

        Map<String, PropertyDescriptor> properties = getProperties(bean.getClass());
        for (PropertyDescriptor propertyDescriptor : properties.values()) {
            Method readMethod = propertyDescriptor.getReadMethod();
            Object propertyValue = readMethod.invoke(bean);

            Method writeMethod = propertyDescriptor.getWriteMethod();
            writeMethod.invoke(clone, propertyValue);
        }

        return clone;
    }

    protected Object newInstance(Class<?> beanClass) throws Exception {

        return beanClass.getDeclaredConstructor().newInstance();
    }

    private void setState(Object bean, Map<String, Object> state) throws Exception {

        Map<String, PropertyDescriptor> propertyDescriptorsByName = getProperties(bean.getClass());

        for (Map.Entry<String, Object> stateEntry : state.entrySet()) {
            String propertyName = stateEntry.getKey();
            PropertyDescriptor propertyDescriptor = propertyDescriptorsByName.get(propertyName);
            if (propertyDescriptor != null) {
                Object propertyValue = state.get(propertyName);
                setProperty(bean, propertyDescriptor, propertyValue);
            }
        }
    }

    protected Object getProperty(Object bean, PropertyDescriptor propertyDescriptor) throws Exception {

        Method readMethod = propertyDescriptor.getReadMethod();
        if (readMethod != null) {
            return readMethod.invoke(bean);
        }
        throw new IllegalStateException("No getter available for " + propertyDescriptor);
    }

    protected void setProperty(Object bean, PropertyDescriptor propertyDescriptor, Object propertyValue) throws Exception {

        Method writeMethod = propertyDescriptor.getWriteMethod();
        if (writeMethod != null) {
            writeMethod.invoke(bean, propertyValue);
        }
    }

    protected Map<String, PropertyDescriptor> getProperties(Class<?> beanType) throws IntrospectionException {

        BeanInfo beanInfo = Introspector.getBeanInfo(beanType, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        Map<String, PropertyDescriptor> propertyDescriptorMap = new LinkedHashMap<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            propertyDescriptorMap.put(propertyDescriptor.getName(), propertyDescriptor);
        }

        return propertyDescriptorMap;
    }

    private Map<String, Object> generateBeanState(Class<?> beanClass) throws IntrospectionException {

        Map<String, Object> beanTestData = new LinkedHashMap<>();

        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod != null) {
                Class<?> propertyType = writeMethod.getParameterTypes()[0];
                ValueGenerator valueGenerator = valueGeneratorRegistry.getValueGenerator(propertyType);
                Object propertyTestValue = valueGenerator.getValue();
                String propertyName = propertyDescriptor.getName();
                beanTestData.put(propertyName, propertyTestValue);
            }
        }

        return beanTestData;
    }

    protected void testToStringContainsAllProperties(Class<?> beanType) throws Exception {

        Object bean1 = newInstance(beanType);
        Map<String, Object> equalState = generateBeanState(beanType);
        setState(bean1, equalState);

        for (Map.Entry<String, Object> entries : equalState.entrySet()) {
            String attribName = entries.getKey();
            Object attribValue = entries.getValue();
            assertTrue(bean1.toString().contains(attribName), "Contains Attribute Name " + attribName);
            assertTrue(bean1.toString().contains(attribValue.toString()), "Contains Attribute Value " + attribValue);
        }

        assertTrue(bean1.toString().contains(beanType.getSimpleName()), "Contains " + beanType.getSimpleName());
    }

    protected void testToStringContainsClassname(Class<?> beanType) throws Exception {

        Object bean1 = newInstance(beanType);
        Map<String, Object> equalState = generateBeanState(beanType);
        setState(bean1, equalState);

        assertTrue(bean1.toString().contains(beanType.getSimpleName()), "Contains " + beanType.getSimpleName());
    }

}
