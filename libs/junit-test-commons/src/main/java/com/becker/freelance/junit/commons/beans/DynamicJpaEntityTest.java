package com.becker.freelance.junit.commons.beans;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class DynamicJpaEntityTest {

    protected abstract Class<? extends Annotation> tableAnnotation();

    protected abstract Class<? extends Annotation> entityAnnotation();

    protected abstract Class<? extends Annotation> columnAnnotation();

    protected abstract Class<? extends Annotation> oneToOneAnnotation();

    protected abstract Class<? extends Annotation> oneToManyAnnotation();

    protected abstract Class<? extends Annotation> manyToOneAnnotation();

    protected abstract Class<? extends Annotation> joinColumnAnnotation();

    protected abstract Class<? extends Annotation> manyToManyAnnotation();

    protected abstract Class<? extends Annotation> transientAnnotation();

    protected abstract Class<?> createEntity();

    @Test
    void testAttributesAreCorrectAnnotated() {

        Class<?> entity = createEntity();

        List<Field> persistentFields = Arrays.stream(entity.getDeclaredFields()).filter(this::isNotTransient).toList();

        for (Field field : persistentFields) {
            List<Class<? extends Annotation>> expectedAnnotations = new ArrayList<>();
            if (field.isAnnotationPresent(columnAnnotation())) {
                expectedAnnotations.add(columnAnnotation());
            } else if (field.isAnnotationPresent(oneToOneAnnotation())) {
                expectedAnnotations.add(oneToOneAnnotation());
                expectedAnnotations.add(joinColumnAnnotation());
            } else if (field.isAnnotationPresent(oneToManyAnnotation())) {
                expectedAnnotations.add(oneToManyAnnotation());
            } else if (field.isAnnotationPresent(manyToOneAnnotation())) {
                expectedAnnotations.add(manyToOneAnnotation());
                expectedAnnotations.add(joinColumnAnnotation());
            } else if (field.isAnnotationPresent(manyToManyAnnotation())) {
                expectedAnnotations.add(manyToManyAnnotation());
            }

            assertFalse(expectedAnnotations.isEmpty(), "Min one Annotation needed for Field " + field.getName());

            for (Class<? extends Annotation> expectedAnnotation : expectedAnnotations) {
                assertTrue(
                        field.isAnnotationPresent(expectedAnnotation),
                        "Field " + field.getName() + " is annotated with " + expectedAnnotation);
            }
        }
    }

    private boolean isNotTransient(Field field) {

        return !(field.isAnnotationPresent(transientAnnotation()) || Modifier.isTransient(field.getModifiers()));
    }

    @Test
    void testClassIsAnnotatedWithEntity() {

        Class<?> entity = createEntity();
        assertTrue(entity.isAnnotationPresent(entityAnnotation()), "Class is annotated with " + entityAnnotation());
    }

    @Test
    void testClassIsAnnotatedWithTable() {

        Class<?> entity = createEntity();
        assertTrue(entity.isAnnotationPresent(tableAnnotation()), "Class is annotated with " + tableAnnotation());
    }
}
