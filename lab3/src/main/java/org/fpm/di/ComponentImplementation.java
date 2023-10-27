package org.fpm.di;

public record ComponentImplementation<T>(Class<T> implementationClass, T instance) {
    public ComponentImplementation(Class<T> implementationClass) {
        this(implementationClass, null);
    }
}
