package com.becker.freelance.junit.commons.beans;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

class DynamicJpaEntityTestTest extends DynamicBeanTestTest {

    @Test
    void testEntityAnnotatedWithEntityPasses() {

        DummyDynamicJpaEntityTest dummyDynamicJpaEntityTest = new DummyDynamicJpaEntityTest() {

            @Override
            protected Class<?> createEntity() {

                return CorrectAnnotatedEntity.class;
            }
        };

        dummyDynamicJpaEntityTest.testClassIsAnnotatedWithEntity();
    }

    @Test
    void testEntityAnnotatedWithTablePasses() {

        DummyDynamicJpaEntityTest dummyDynamicJpaEntityTest = new DummyDynamicJpaEntityTest() {

            @Override
            protected Class<?> createEntity() {

                return CorrectAnnotatedEntity.class;
            }
        };

        dummyDynamicJpaEntityTest.testClassIsAnnotatedWithTable();
    }

    @Test
    void testEntityAttributesAnnotatedWithColumnPasses() {

        DummyDynamicJpaEntityTest dummyDynamicJpaEntityTest = new DummyDynamicJpaEntityTest() {

            @Override
            protected Class<?> createEntity() {

                return CorrectAnnotatedEntity.class;
            }
        };

        dummyDynamicJpaEntityTest.testAttributesAreCorrectAnnotated();
    }

    @Test
    void testEntityAttributesNotAnnotatedWithColumnPasses() {

        DummyDynamicJpaEntityTest dummyDynamicJpaEntityTest = new DummyDynamicJpaEntityTest() {

            @Override
            protected Class<?> createEntity() {

                return NotCorrectAnnotatedEntity.class;
            }
        };

        Assertions.assertThrows(AssertionFailedError.class, dummyDynamicJpaEntityTest::testAttributesAreCorrectAnnotated);
    }

    @Test
    void testEntityNotAnnotatedWithEntityFails() {

        DummyDynamicJpaEntityTest dummyDynamicJpaEntityTest = new DummyDynamicJpaEntityTest() {

            @Override
            protected Class<?> createEntity() {

                return NotCorrectAnnotatedEntity.class;
            }
        };

        Assertions.assertThrows(AssertionFailedError.class, dummyDynamicJpaEntityTest::testClassIsAnnotatedWithEntity);
    }

    @Test
    void testEntityNotAnnotatedWithTableFails() {

        DummyDynamicJpaEntityTest dummyDynamicJpaEntityTest = new DummyDynamicJpaEntityTest() {

            @Override
            protected Class<?> createEntity() {

                return NotCorrectAnnotatedEntity.class;
            }
        };

        Assertions.assertThrows(AssertionFailedError.class, dummyDynamicJpaEntityTest::testClassIsAnnotatedWithTable);
    }

    @Test
    void testWithNotCorrectOneToOneAttributeInEntityFails() {

        DummyDynamicJpaEntityTest dummyDynamicJpaEntityTest = new DummyDynamicJpaEntityTest() {

            @Override
            protected Class<?> createEntity() {

                return NotCorrectOneToOneAnnotatedEntity.class;
            }
        };

        Assertions.assertThrows(AssertionFailedError.class, dummyDynamicJpaEntityTest::testAttributesAreCorrectAnnotated);
    }

    @Test
    void testWithCorrectManyToOneAttribute() {

        DummyDynamicJpaEntityTest dummyDynamicJpaEntityTest = new DummyDynamicJpaEntityTest() {

            @Override
            protected Class<?> createEntity() {

                return ManyToOneEntity.class;
            }
        };

        dummyDynamicJpaEntityTest.testAttributesAreCorrectAnnotated();
    }

    @Test
    void testWithCorrectManyToManyAttribute() {

        DummyDynamicJpaEntityTest dummyDynamicJpaEntityTest = new DummyDynamicJpaEntityTest() {

            @Override
            protected Class<?> createEntity() {

                return ManyToManyEntity.class;
            }
        };

        dummyDynamicJpaEntityTest.testAttributesAreCorrectAnnotated();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Table {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Entity {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Column {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface OneToOne {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface OneToMany {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ManyToOne {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface JoinTable {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface JoinColumn {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ManyToMany {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Transient {
    }

    private static class DummyDynamicJpaEntityTest extends DynamicJpaEntityTest {

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

        @Override
        protected Class<?> createEntity() {

            return null;
        }
    }

    @Entity
    protected static class InnerEntityObject {

    }

    @Table
    @Entity
    protected static class CorrectAnnotatedEntity {

        @Column
        private int col1;

        @Column
        private int col2;

        @OneToOne
        @JoinColumn
        private InnerEntityObject col3;

        @OneToMany
        @JoinTable
        private List<InnerEntityObject> col4;
    }

    protected static class NotCorrectAnnotatedEntity {

        @Column
        private int col1;

        private int col2;
    }

    @Entity
    @Table
    protected static class NotCorrectOneToOneAnnotatedEntity {

        @OneToOne
        private InnerEntityObject col1;
    }

    @Entity
    @Table
    protected static class ManyToOneEntity {

        @ManyToOne
        @JoinColumn
        private InnerEntityObject col1;
    }

    @Entity
    @Table
    protected static class ManyToManyEntity {

        @ManyToMany
        private InnerEntityObject col1;
    }
}
