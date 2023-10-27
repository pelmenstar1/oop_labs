package org.fpm.di;

public record ComponentGraphVertex<T>(Class<T> baseClass, ComponentImplementation<? extends T> implementation) {
}
