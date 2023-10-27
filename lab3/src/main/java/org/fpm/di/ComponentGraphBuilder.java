package org.fpm.di;

import java.util.ArrayList;
import java.util.List;

public class ComponentGraphBuilder {
    private final List<ComponentGraphVertex<?>> vertexList = new ArrayList<>();

    public<T> void addComponent(Class<T> clazz) {
        addComponent(clazz, clazz);
    }

    public<T> void addComponent(Class<T> clazz, Class<? extends T> implClass) {
        ensureNoVertexWithBaseClass(clazz);

        var impl = new ComponentImplementation<>(implClass);
        vertexList.add(new ComponentGraphVertex<>(clazz, impl));
    }

    public<T> void addComponent(Class<T> clazz, T instance) {
        ensureNoVertexWithBaseClass(clazz);

        var impl = new ComponentImplementation<>(clazz, instance);
        vertexList.add(new ComponentGraphVertex<>(clazz, impl));
    }

    public ComponentGraph build() {
        return new ComponentGraph(vertexList.toArray(new ComponentGraphVertex[0]));
    }

    private void ensureNoVertexWithBaseClass(Class<?> baseClass) {
        if (containsVertexWithExactBaseClass(baseClass)) {
            throw new IllegalStateException("There is already a binding for class " + baseClass);
        }
    }

    public boolean containsVertexWithExactBaseClass(Class<?> baseClass) {
        for (ComponentGraphVertex<?> vertex : vertexList) {
            if (vertex.baseClass().equals(baseClass)) {
                return true;
            }
        }

        return false;
    }
 }
