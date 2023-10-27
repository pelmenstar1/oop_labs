package org.fpm.di;

import java.util.Arrays;

public final class ComponentGraph {
    private final ComponentGraphVertex<?>[] vertices;

    public ComponentGraph(ComponentGraphVertex<?>[] vertices) {
        this.vertices = vertices;
    }

    public<T> ComponentImplementation<? extends T> getExactImplementation(Class<T> baseClass) {
        ComponentImplementation<? extends T> impl = findImplementationWithBaseClass(baseClass);
        if (impl == null) {
            throw new RuntimeException("No binding for class " + baseClass);
        }

        while(true) {
            ComponentImplementation<? extends T> nextImpl = findImplementationWithBaseClass(impl.implementationClass());
            if (nextImpl == null) {
                return impl;
            }

            if (nextImpl.implementationClass().equals(impl.implementationClass())) {
                return nextImpl;
            }

            impl = nextImpl;
        }
    }

    @SuppressWarnings("unchecked")
    private<T> ComponentImplementation<T> findImplementationWithBaseClass(Class<T> baseClass) {
        for (ComponentGraphVertex<?> vertex : vertices) {
            if (vertex.baseClass().equals(baseClass)) {
                return (ComponentImplementation<T>) vertex.implementation();
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ComponentGraph other && Arrays.equals(vertices, other.vertices);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vertices);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("ComponentGraph [");
        for (int i = 0; i < vertices.length; i++) {
            ComponentGraphVertex<?> vertex = vertices[i];
            ComponentImplementation<?> impl = vertex.implementation();

            sb.append('{');
            sb.append(vertex.baseClass().getSimpleName());
            sb.append(" -> ");
            sb.append(impl.implementationClass().getSimpleName());
            if (impl.instance() != null) {
                sb.append(" (instance=")
                    .append(impl.instance())
                    .append(')');
            }

            sb.append('}');

            if (i < vertices.length - 1) {
                sb.append(", ");
            }
        }

        sb.append(']');

        return sb.toString();
    }
}
