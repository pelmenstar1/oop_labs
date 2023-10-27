package org.fpm.di;

public class DefaultBinder implements Binder {
    private final ComponentGraphBuilder builder;

    public DefaultBinder(ComponentGraphBuilder builder) {
        this.builder = builder;
    }

    @Override
    public <T> void bind(Class<T> clazz) {
        builder.addComponent(clazz);
    }

    @Override
    public <T> void bind(Class<T> clazz, Class<? extends T> implementation) {
        builder.addComponent(clazz, implementation);
    }

    @Override
    public <T> void bind(Class<T> clazz, T instance) {
        builder.addComponent(clazz, instance);
    }
}
