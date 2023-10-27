package org.fpm.di;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultContainer implements Container {
    private final ComponentGraph graph;
    private final Map<Class<?>, Object> singletons;

    public DefaultContainer(ComponentGraph graph) {
        this.graph = graph;
        singletons = new HashMap<>();
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        ComponentImplementation<? extends T> impl = graph.getExactImplementation(clazz);
        T implInstance = impl.instance();

        return Objects.requireNonNullElseGet(
            implInstance,
            () -> getComponentExact(impl.implementationClass())
        );
    }

    @SuppressWarnings("unchecked")
    private <T> T getComponentExact(Class<T> clazz) {
        if (clazz.isAnnotationPresent(Singleton.class)) {
            return (T)singletons.computeIfAbsent(clazz, this::createComponent);
        }

        return createComponent(clazz);
    }

    private <T> T createComponent(Class<T> clazz) {
        validateClass(clazz);

        Constructor<?>[] constructors = clazz.getConstructors();

        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return createObjectWithConstructor(constructor);
            }

            if (constructor.isAnnotationPresent(Inject.class)) {
                Object[] params = getComponents(constructor.getParameterTypes());

                return createObjectWithConstructor(constructor, params);
            }
        }

        throw new RuntimeException("Cannot find an appropriate constructor");
    }

    private Object[] getComponents(Class<?>[] classes) {
        var values = new Object[classes.length];

        for (int i = 0; i < classes.length; i++) {
            values[i] = getComponent(classes[i]);
        }

        return values;
    }

    @SuppressWarnings("unchecked")
    private static<T> T createObjectWithConstructor(Constructor<?> constructor, Object... params) {
        try {
            return (T)constructor.newInstance(params);
        } catch (InstantiationException e) {
            throw new RuntimeException("The class is abstract", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Constructor is not public", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Constructor threw exception", e);
        }
    }

    private static void validateClass(Class<?> c) {
        int mods = c.getModifiers();

        if (c.isPrimitive()) {
            throw new RuntimeException("Cannot instantiate a primitive type");
        }
        if (Modifier.isAbstract(mods)) {
            throw new RuntimeException("Cannot instantiate abstract class");
        }
        if (!Modifier.isPublic(mods)) {
            throw new RuntimeException("The class is not public");
        }
    }
}
