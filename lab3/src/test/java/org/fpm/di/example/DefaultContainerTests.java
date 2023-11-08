package org.fpm.di.example;


import org.fpm.di.Configuration;
import org.fpm.di.Container;
import org.fpm.di.DefaultEnvironment;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.*;

public class DefaultContainerTests {
    public static class Class1 {}
    public static class Class2 extends Class1 {}
    public static class Class3 extends Class2 {}

    public static class Class4 {}

    private static class PrivateClass {}
    public static class ClassWithNoPublicConstructors {
        private ClassWithNoPublicConstructors() {}
    }
    public static abstract class AbstractClass {}
    public interface InterfaceClass {}
    public static class ClassWithNoInjectConstructor {
        public ClassWithNoInjectConstructor(int value) {}
    }

    public static class InjectFieldClass {
        public final Class4 class4;

        @Inject
        public Class1 class1;

        @Inject
        public InjectFieldClass(Class4 value) {
            this.class4 = value;
        }
    }

    public static class InjectFinalFieldClass {
        @Inject
        public final Class1 class1;

        public InjectFinalFieldClass() {
            class1 = new Class1();
        }
    }

    public Container createContainer(Configuration conf) {
        return new DefaultEnvironment().configure(conf);
    }

    @Test
    public void injectClass3ThroughClass1() {
        Container container = createContainer(binder -> {
           binder.bind(Class1.class, Class2.class);
           binder.bind(Class2.class, Class3.class);
           binder.bind(Class3.class);
        });

        Class1 result = container.getComponent(Class1.class);
        assertEquals(Class3.class, result.getClass());
    }

    @Test
    public void injectClass3ThroughClass2WithGivenInstance() {
        final Class3 class3Instance = new Class3();
        Container container = createContainer(binder -> {
            binder.bind(Class1.class, Class2.class);
            binder.bind(Class2.class, Class3.class);
            binder.bind(Class3.class, class3Instance);
        });

        Class2 result = container.getComponent(Class2.class);
        assertSame(result, class3Instance);
    }

    private void invalidClassTestHelper(Class<?> c) {
        Container container = createContainer(binder -> binder.bind(c));
        assertThrows(RuntimeException.class, () -> container.getComponent(c));
    }

    @Test
    public void invalidClassTest() {
        invalidClassTestHelper(PrivateClass.class);
        invalidClassTestHelper(ClassWithNoPublicConstructors.class);
        invalidClassTestHelper(InterfaceClass.class);
        invalidClassTestHelper(AbstractClass.class);
        invalidClassTestHelper(int.class); // Primitive class
        invalidClassTestHelper(ClassWithNoInjectConstructor.class);
    }

    @Test
    public void noBindingTest() {
        var container = createContainer(binder -> {});
        assertThrows(RuntimeException.class, () -> container.getComponent(Object.class));
    }

    @Test
    public void injectFieldTest() {
        var container = createContainer(binder -> {
            binder.bind(Class1.class, Class3.class);
            binder.bind(Class4.class);
            binder.bind(InjectFieldClass.class);
        });

        InjectFieldClass result = container.getComponent(InjectFieldClass.class);
        assertEquals(Class3.class, result.class1.getClass());
        assertNotNull(result.class4);
    }

    @Test
    public void injectFinalFieldTest() {
        var container = createContainer(binder -> {
            binder.bind(Class1.class);
            binder.bind(InjectFinalFieldClass.class);
        });

        assertThrows(RuntimeException.class, () -> container.getComponent(InjectFinalFieldClass.class));
    }

    public void cycleDependencyTestHelper(Class<?>[] classes, Configuration config) {
        var container = createContainer(config);

        for (Class<?> c : classes) {
            assertThrows(
                "Cycle dependency detected",
                RuntimeException.class,
                () -> container.getComponent(c)
            );
        }
    }

    public void cycleDependencyTestHelper(Class<?>... classes) {
        cycleDependencyTestHelper(classes, binder -> {
            for (Class<?> c : classes) {
                binder.bind(c);
            }
        });
    }

    @Test
    public void cycleDependencyTest() {
        cycleDependencyTestHelper(
            CycleDependencies.ConstructorSimple.A.class,
            CycleDependencies.ConstructorSimple.B.class
        );

        cycleDependencyTestHelper(
            CycleDependencies.ConstructorTwoLevel.A.class,
            CycleDependencies.ConstructorTwoLevel.B.class,
            CycleDependencies.ConstructorTwoLevel.C.class
        );

        cycleDependencyTestHelper(
            new Class[]{
                CycleDependencies.ConstructorTwoLevelInheritance.A1.class,
                CycleDependencies.ConstructorTwoLevelInheritance.A2.class,
                CycleDependencies.ConstructorTwoLevelInheritance.B.class,
                CycleDependencies.ConstructorTwoLevelInheritance.C.class,
            },
            binder -> {
                binder.bind(CycleDependencies.ConstructorTwoLevelInheritance.A1.class, CycleDependencies.ConstructorTwoLevelInheritance.A2.class);
                binder.bind(CycleDependencies.ConstructorTwoLevelInheritance.B.class);
                binder.bind(CycleDependencies.ConstructorTwoLevelInheritance.C.class);
            }
        );

        cycleDependencyTestHelper(
            CycleDependencies.InjectFields.A.class,
            CycleDependencies.InjectFields.B.class,
            CycleDependencies.InjectFields.C.class
        );
    }
}
