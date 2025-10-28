package com.becker.freelance.component.prediction.storage.adapter.jpa;

import com.becker.freelance.junit.commons.beans.DynamicJpaEntityTest;
import jakarta.persistence.*;

import java.lang.annotation.Annotation;

public abstract class StorageJpaEntityTest extends DynamicJpaEntityTest {

    @Override
    protected Class<? extends Annotation> tableAnnotation() {
        return Table.class;
    }

    @Override
    protected Class<? extends Annotation> entityAnnotation() {
        return Entity.class;
    }

    @Override
    protected Class<? extends Annotation> columnAnnotation() {
        return Column.class;
    }

    @Override
    protected Class<? extends Annotation> oneToOneAnnotation() {
        return OneToOne.class;
    }

    @Override
    protected Class<? extends Annotation> oneToManyAnnotation() {
        return OneToMany.class;
    }

    @Override
    protected Class<? extends Annotation> manyToOneAnnotation() {
        return ManyToOne.class;
    }

    @Override
    protected Class<? extends Annotation> joinColumnAnnotation() {
        return JoinColumn.class;
    }

    @Override
    protected Class<? extends Annotation> manyToManyAnnotation() {
        return ManyToMany.class;
    }

    @Override
    protected Class<? extends Annotation> transientAnnotation() {
        return Transient.class;
    }
}
