package org.fpm.di;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

public class DefaultContainer implements Container {
    private final ComponentGraph graph;
    private final Map<Class<?>, Object> singletons;
    private final Set<Class<?>> cycleDependencyCheckedClasses;

    public DefaultContainer(ComponentGraph graph) {
        this.graph = graph;
        singletons = new HashMap<>();
        cycleDependencyCheckedClasses = new HashSet<>();
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

        if (!cycleDependencyCheckedClasses.contains(clazz)) {
            checkCycleDependencies(clazz);
            cycleDependencyCheckedClasses.add(clazz);
        }

        return createComponent(clazz);
    }

    private <T> T createComponent(Class<T> clazz) {
        validateClass(clazz);

        Constructor<T> cons = selectConstructor(clazz);
        Object[] params = getComponents(cons.getParameterTypes());

        return createObjectAndInjectFields(cons, params);
    }

    @SuppressWarnings("unchecked")
    private static<T> Constructor<T> selectConstructor(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();

        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0 || constructor.isAnnotationPresent(Inject.class)) {
                return (Constructor<T>)constructor;
            }
        }

        throw new RuntimeException("Cannot find an appropriate constructor");
    }

    public static Collection<Field> selectInjectFields(Class<?> clazz) {
        var result = new ArrayList<Field>();

        Field[] instanceFields = clazz.getFields();

        for (Field field : instanceFields) {
            int mods = field.getModifiers();
            if (Modifier.isStatic(mods)) {
                continue;
            }

            if (field.isAnnotationPresent(Inject.class)) {
                result.add(field);
            }
        }

        return result;
    }

    private <T> T createObjectAndInjectFields(Constructor<T> constructor, Object... params) {
        try {
            T result = constructor.newInstance(params);
            injectFields(result);

            return result;
        } catch (InstantiationException e) {
            throw new RuntimeException("The class is abstract", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Constructor is not public", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Constructor threw exception", e);
        }
    }

    private void injectFields(Object obj) {
        for (Field field : selectInjectFields(obj.getClass())) {
            if (Modifier.isFinal(field.getModifiers())) {
                throw new RuntimeException("Field annotated with Inject is final");
            }

            Class<?> fieldType = field.getType();
            Object fieldValue = getComponent(fieldType);

            if (field.trySetAccessible()) {
                try {
                    field.set(obj, fieldValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot set a field");
                }
            } else {
                throw new RuntimeException("Field annotated with Inject is private");
            }
        }
    }

    private Object[] getComponents(Class<?>[] classes) {
        var values = new Object[classes.length];

        for (int i = 0; i < classes.length; i++) {
            values[i] = getComponent(classes[i]);
        }

        return values;
    }

    private Collection<Class<?>> unionDependencies(Constructor<?> cons, Collection<Field> injectFields) {
        var result = new ArrayList<Class<?>>(cons.getParameterCount() + injectFields.size());
        result.addAll(Arrays.asList(cons.getParameterTypes()));

        for (Field field : injectFields) {
            result.add(field.getType());
        }

        return result;
    }

    private Collection<Class<?>> getDirectDependencies(Class<?> c) {
        Constructor<?> cons = selectConstructor(c);
        Collection<Field> injectFields = selectInjectFields(c);

        var deps = unionDependencies(cons, injectFields);
        var result = new HashSet<Class<?>>(deps.size());

        for (Class<?> dep : deps) {
            ComponentImplementation<?> impl = graph.getExactImplementation(dep);
            if (impl.instance() == null) {
                result.add(impl.implementationClass());
            }
        }

        return result;
    }

    private void checkCycleDependencies(Class<?> c) {
        var totalDeps = new HashSet<Class<?>>();
        totalDeps.add(c);

        checkCycleDependencies(c, totalDeps);
    }

    private void checkCycleDependencies(Class<?> c, HashSet<Class<?>> totalDeps) {
        Collection<Class<?>> directDeps = getDirectDependencies(c);
        for (Class<?> dep : directDeps) {
            boolean isAdded = totalDeps.add(dep);
            if (!isAdded) {
                throw new RuntimeException("Cycle dependency detected");
            }
        }

        for (Class<?> dep : directDeps) {
            checkCycleDependencies(dep, totalDeps);
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
