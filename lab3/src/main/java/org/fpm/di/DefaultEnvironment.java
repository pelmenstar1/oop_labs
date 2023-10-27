package org.fpm.di;

public class DefaultEnvironment implements Environment {
    @Override
    public Container configure(Configuration configuration) {
        var graphBuilder = new ComponentGraphBuilder();
        var binder = new DefaultBinder(graphBuilder);
        configuration.configure(binder);

        ComponentGraph graph = graphBuilder.build();

        return new DefaultContainer(graph);
    }
}
