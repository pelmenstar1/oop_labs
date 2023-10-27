package org.fpm.di.example;

import org.fpm.di.Binder;
import org.fpm.di.DefaultEnvironment;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.function.Consumer;

public class ComponentGraphBuilderTests {
    public static class Class1 {}
    public static class Class2 extends Class1 {}
    public static class Class3 extends Class1 {}

    private void assertConfigurationThrows(Consumer<Binder> binderConsumer) {
        var env = new DefaultEnvironment();

        assertThrows(IllegalStateException.class, () -> env.configure(binderConsumer::accept));
    }

    @Test
    public void validateStructureOnClassBindTest() {
        assertConfigurationThrows(binder -> {
            binder.bind(Class1.class, Class2.class);
            binder.bind(Class1.class, Class3.class);
        });
    }

    @Test
    public void validateStructureOnInstanceBindTest() {
        assertConfigurationThrows(binder -> {
            binder.bind(Class1.class, Class2.class);
            binder.bind(Class1.class, new Class1());
        });
    }
}
